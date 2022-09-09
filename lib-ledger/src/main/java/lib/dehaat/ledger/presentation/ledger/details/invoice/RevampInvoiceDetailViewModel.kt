package lib.dehaat.ledger.presentation.ledger.details.invoice

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.helper.callInViewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import lib.dehaat.ledger.domain.usecases.GetInvoiceDetailUseCase
import lib.dehaat.ledger.entities.revamp.invoice.InvoiceDataEntity
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.invoice.InvoiceDetailsViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar

@HiltViewModel
class RevampInvoiceDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInvoiceDetailUseCase: GetInvoiceDetailUseCase,
    private val mapper: LedgerViewDataMapper
) : BaseViewModel() {

    val ledgerId by lazy { savedStateHandle.get<String>(LedgerConstants.KEY_LEDGER_ID) ?: "" }
    val source by lazy { savedStateHandle.get<String>(LedgerConstants.KEY_SOURCE) ?: "" }

    private val viewModelState = MutableStateFlow(InvoiceDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUIState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUIState()
        )

    init {
        getInvoiceDetailFromServer()
    }

    private fun getInvoiceDetailFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getInvoiceDetailUseCase.getInvoiceDetails(ledgerId)
            calledAPI()
            processInvoiceDetailResponse(response)
        }
    }

    private fun processInvoiceDetailResponse(result: APIResultEntity<InvoiceDataEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendFailureEvent) { entity ->
            entity?.let { invoiceDataEntity ->
                val invoiceDetailsViewData = mapper.toInvoiceDetailsViewData(invoiceDataEntity)
                viewModelState.update {
                    it.copy(
                        isSuccess = true,
                        invoiceDetailsViewData = invoiceDetailsViewData
                    )
                }
            }
        }
    }

    private fun calledAPI() = updateProgressDialog(false)

    private fun callingAPI() = updateProgressDialog(true)

    fun updateProgressDialog(show: Boolean) = viewModelState.update {
        it.copy(isLoading = show)
    }

    private fun sendFailureEvent(message: String) {
        viewModelState.update {
            it.copy(
                isError = true,
                errorMessage = message
            )
        }
    }

    companion object {
        fun getBundle(ledgerId: String, source: String) = Bundle().apply {
            putString(LedgerConstants.KEY_LEDGER_ID, ledgerId)
            putString(LedgerConstants.KEY_SOURCE, source)
        }
    }
}
