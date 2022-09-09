package lib.dehaat.ledger.presentation.ledger.revamp.state.invoicelist

import lib.dehaat.ledger.presentation.ledger.revamp.state.UIState
import lib.dehaat.ledger.presentation.model.invoicelist.InvoiceListViewData

data class InvoiceListViewModelState(
    val interestApproachingInvoices: List<InvoiceListViewData>? = null,
    val interestApproachedInvoices: List<InvoiceListViewData>? = null,
    val interestApproachingLoading: Boolean = false,
    val interestApproachedLoading: Boolean = false,
    val interestApproachedMinimized: Boolean = true,
    val interestApproachingMinimized: Boolean = true,
    val interestApproachedExhausted: Boolean = false,
    val interestApproachingExhausted: Boolean = false,
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false
) {
    fun toUIState() = InvoiceListUIState(
        interestApproachingInvoices = interestApproachingInvoices?.let {
            InvoiceUiState(
                loadingState = when {
                    interestApproachingLoading -> InvoiceLoadingState.Loading
                    !interestApproachingExhausted -> InvoiceLoadingState.LoadMore
                    else -> InvoiceLoadingState.Minimize
                },
                invoices = if (interestApproachingMinimized && it.size > 5) {
                    it.subList(0, 5)
                } else {
                    it
                }
            )
        },
        interestApproachedInvoices = interestApproachedInvoices?.let {
            InvoiceUiState(
                loadingState = when {
                    interestApproachedLoading -> InvoiceLoadingState.Loading
                    !interestApproachedExhausted -> InvoiceLoadingState.LoadMore
                    else -> InvoiceLoadingState.Minimize
                },
                invoices = if (interestApproachedMinimized && it.size > 5) {
                    it.subList(0, 5)
                } else {
                    it
                }
            )
        },
        state = when {
            isSuccess -> UIState.SUCCESS
            isLoading -> UIState.LOADING
            isError -> UIState.ERROR(errorMessage)
            else -> UIState.SUCCESS
        }
    )
}

data class InvoiceListUIState(
    val interestApproachingInvoices: InvoiceUiState?,
    val interestApproachedInvoices: InvoiceUiState?,
    val state: UIState
)

data class InvoiceUiState(
    val loadingState: InvoiceLoadingState,
    val invoices: List<InvoiceListViewData>
)

sealed class InvoiceLoadingState {
    object LoadMore : InvoiceLoadingState()
    object Minimize : InvoiceLoadingState()
    object Loading : InvoiceLoadingState()
}
