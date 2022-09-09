package lib.dehaat.ledger.presentation.ledger.revamp.state.invoice

import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.model.revamp.invoice.InvoiceDetailsViewData

data class InvoiceDetailsViewModelState(
    val invoiceDetailsViewData: InvoiceDetailsViewData? = null,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
) {
    fun toUIState() = InvoiceDetailsUIState(
        invoiceDetailsViewData = invoiceDetailsViewData,
        state = when {
            isSuccess -> UIState.SUCCESS
            isError -> UIState.ERROR(errorMessage)
            isLoading -> UIState.LOADING
            else -> UIState.SUCCESS
        }
    )
}

data class InvoiceDetailsUIState(
    val invoiceDetailsViewData: InvoiceDetailsViewData? = null,
    val state: UIState
)
