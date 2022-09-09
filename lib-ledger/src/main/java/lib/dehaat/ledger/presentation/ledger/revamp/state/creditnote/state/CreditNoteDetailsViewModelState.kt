package lib.dehaat.ledger.presentation.ledger.revamp.state.creditnote.state

import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.model.revamp.creditnote.CreditNoteDetailsViewData

data class CreditNoteDetailsViewModelState(
    val viewData: CreditNoteDetailsViewData? = null,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
) {
    fun toUIState() = CreditNoteDetailsUIState(
        viewData = viewData,
        state = when {
            isSuccess -> UIState.SUCCESS
            isError -> UIState.ERROR(errorMessage)
            isLoading -> UIState.LOADING
            else -> UIState.SUCCESS
        }
    )
}

data class CreditNoteDetailsUIState(
    val viewData: CreditNoteDetailsViewData?,
    val state: UIState
)
