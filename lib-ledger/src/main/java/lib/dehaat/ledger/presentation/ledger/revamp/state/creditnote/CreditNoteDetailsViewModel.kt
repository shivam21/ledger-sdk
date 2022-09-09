package lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote

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
import lib.dehaat.ledger.domain.usecases.GetCreditNoteDetailUseCase
import lib.dehaat.ledger.entities.detail.creditnote.CreditNoteDetailEntity
import lib.dehaat.ledger.presentation.LedgerConstants
import lib.dehaat.ledger.presentation.common.BaseViewModel
import lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.state.CreditNoteDetailsViewModelState
import lib.dehaat.ledger.presentation.mapper.LedgerViewDataMapper
import lib.dehaat.ledger.util.processAPIResponseWithFailureSnackBar

@HiltViewModel
class CreditNoteDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCreditNoteDetailUseCase: GetCreditNoteDetailUseCase,
    private val mapper: LedgerViewDataMapper
) : BaseViewModel() {

    val ledgerId by lazy { savedStateHandle.get<String>(LedgerConstants.KEY_LEDGER_ID) ?: "" }

    private val viewModelState = MutableStateFlow(CreditNoteDetailsViewModelState())
    val uiState = viewModelState
        .map { it.toUIState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUIState()
        )

    init {
        getCreditNoteDetailFromServer()
    }

    private fun getCreditNoteDetailFromServer() {
        callInViewModelScope {
            callingAPI()
            val response = getCreditNoteDetailUseCase.invoke(ledgerId)
            calledAPI()
            processCreditNoteDetailResponse(response)
        }
    }

    private fun processCreditNoteDetailResponse(result: APIResultEntity<CreditNoteDetailEntity?>) {
        result.processAPIResponseWithFailureSnackBar(::sendFailureEvent) { entity ->
            entity?.let { creditSummaryEntity ->
                val viewData = mapper.toCreditNoteDetailsDataEntity(creditSummaryEntity)
                viewModelState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true,
                        viewData = viewData
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
        fun getBundle(ledgerId: String) = Bundle().apply {
            putString(LedgerConstants.KEY_LEDGER_ID, ledgerId)
        }
    }
}
