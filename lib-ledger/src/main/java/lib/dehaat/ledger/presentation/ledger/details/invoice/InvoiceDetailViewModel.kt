package lib.dehaat.ledger.presentation.ledger.details.invoice

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import lib.dehaat.ledger.domain.usecases.GetInvoiceDetailUseCase
import lib.dehaat.ledger.domain.usecases.GetInvoiceDownloadUseCase
import lib.dehaat.ledger.entities.detail.invoice.InvoiceDetailDataEntity
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.presentation.LedgerConstants.KEY_LEDGER_ID
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.common.UiEvent
import lib.dehaat.ledger.presentation.ledger.details.invoice.state.InvoiceDetailViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData
import lib.dehaat.ledger.presentation.processAPIResponseWithFailureSnackBar
import lib.dehaat.ledger.util.DownloadFileUtil
import lib.dehaat.ledger.util.FileUtils
import java.io.File
import javax.inject.Inject

@HiltViewModel
class InvoiceDetailViewModel @Inject constructor(
    private val getInvoiceDetailUseCase: GetInvoiceDetailUseCase,
    private val getInvoiceDownloadUseCase: GetInvoiceDownloadUseCase,
    private val mapper: LedgerViewDataMapper,
    private val downloadFileUtil: DownloadFileUtil,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val ledgerId by lazy {
        savedStateHandle.get<String>(KEY_LEDGER_ID) ?: throw Exception(
            "Ledger id should not null"
        )
    }

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> get() = _uiEvent

    private val invoiceDownloadData = InvoiceDownloadData()

    private val viewModelState = MutableStateFlow(InvoiceDetailViewModelState())
    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        getInvoiceDetailFromServer()
    }

    private fun getInvoiceDetailFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getInvoiceDetailUseCase.invoke(ledgerId)
            calledAPI()
            processInvoiceDetailResponse(response)
        }
    }

    private fun processInvoiceDetailResponse(result: APIResultEntity<InvoiceDetailDataEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) {
            it?.let { creditSummaryEntity ->
                val invoiceDetailViewData = mapper.toInvoiceDetailDataViewData(creditSummaryEntity)
                viewModelState.update { it ->
                    it.copy(isLoading = false, invoiceDetailDataViewData = invoiceDetailViewData)
                }
            }
        }
    }

    private fun sendShowSnackBarEvent(message: String) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowSnackbar(message))
        }
    }

    fun downloadInvoice(
        erpId: String?,
        source: String,
        downloadDirectory: File,
        invoiceDownloadStatus: (InvoiceDownloadData) -> Unit
    ) = callInViewModelScope {
        invoiceDownloadData.apply {
            partnerId = ledgerId
            invoiceId = erpId.toString()
        }
        val identityId = when (source) {
            "SAP" -> erpId?.substringAfterLast('$')
            "ODOO" -> erpId
            else -> null
        }
        identityId?.let {
            getDownloadInvoice(it, source, downloadDirectory, invoiceDownloadStatus)
        }
    }

    fun updateProgressDialog(show: Boolean) = viewModelState.update {
        it.copy(isLoading = show)
    }

    private fun getDownloadInvoice(
        identityId: String,
        source: String,
        downloadDirectory: File,
        invoiceDownloadStatus: (InvoiceDownloadData) -> Unit
    ) = callInViewModelScope {
        updateProgressDialog(true)
        val result = getInvoiceDownloadUseCase.invoke(identityId, source)
        result.processAPIResponseWithFailureSnackBar(::sendShowSnackBarEvent) {
            it?.let { invoiceDownloadDataEntity ->
                when (invoiceDownloadDataEntity.source) {
                    "SAP" -> {
                        updateProgressDialog(false)
                        FileUtils.getFileFromBase64(
                            base64 = invoiceDownloadDataEntity.pdf.orEmpty(),
                            fileType = invoiceDownloadDataEntity.docType,
                            fileName = identityId,
                            dir = downloadDirectory
                        )?.let {
                            invoiceDownloadData.filePath = "${downloadDirectory.path}/${identityId.substringAfterLast('$')}.pdf"
                            invoiceDownloadStatus(invoiceDownloadData)
                        } ?: kotlin.run {
                            invoiceDownloadData.isFailed = true
                            invoiceDownloadStatus(invoiceDownloadData)
                        }
                    }
                    "ODOO" -> {
                        updateProgressDialog(false)
                        invoiceDownloadDataEntity.fileName
                            ?: return@processAPIResponseWithFailureSnackBar
                        downloadFile(
                            invoiceDownloadDataEntity.fileName,
                            downloadDirectory,
                            invoiceDownloadStatus
                        )
                    }
                    else -> {
                        invoiceDownloadData.isFailed = true
                        invoiceDownloadStatus(invoiceDownloadData)
                        updateProgressDialog(false)
                    }
                }
            }
        }
    }

    private fun downloadFile(
        identityId: String,
        downloadDirectory: File,
        invoiceDownloadStatus: (InvoiceDownloadData) -> Unit
    ) = callInViewModelScope {
        updateProgressDialog(true)
        downloadFileUtil.downloadFile(
            downloadDirectory,
            identityId,
            LedgerSDK.bucket
        )?.setTransferListener(
            object : TransferListener {
                override fun onStateChanged(id: Int, state: TransferState?) {
                    if (state == TransferState.COMPLETED) {
                        updateProgressDialog(false)
                        invoiceDownloadData.filePath = "${downloadDirectory.path}/${identityId.substringAfterLast('$')}.pdf"
                        invoiceDownloadStatus(invoiceDownloadData)
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) = Unit

                override fun onError(id: Int, ex: Exception?) {
                    ex?.printStackTrace()
                    invoiceDownloadData.isFailed = true
                    invoiceDownloadStatus(invoiceDownloadData)
                    updateProgressDialog(false)
                }
            }
        )
    }

    private fun calledAPI() {
        viewModelState.update {
            it.copy(isLoading = false)
        }
    }

    private fun callingAPI() {
        viewModelState.update {
            it.copy(isLoading = true)
        }
    }
}
