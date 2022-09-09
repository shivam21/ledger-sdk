package lib.dehaat.ledger.presentation.ledger.details.invoice.state

import lib.dehaat.ledger.presentation.model.detail.invoice.InvoiceDetailDataViewData

data class InvoiceDetailViewModelState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val invoiceDetailDataViewData: InvoiceDetailDataViewData? = null
) {
    fun toUiState() = InvoiceDetailUIState(
        isLoading = isLoading,
        isError = isError,
        invoiceDetailDataViewData = invoiceDetailDataViewData
    )
}

data class InvoiceDetailUIState(
    val isLoading: Boolean,
    val isError: Boolean,
    val invoiceDetailDataViewData: InvoiceDetailDataViewData?
)