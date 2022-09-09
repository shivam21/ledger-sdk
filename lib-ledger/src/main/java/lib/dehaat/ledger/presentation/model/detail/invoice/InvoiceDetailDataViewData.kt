package lib.dehaat.ledger.presentation.model.detail.invoice

data class InvoiceDetailDataViewData(
    val summary: SummaryViewData,
    val loans: List<LoanViewData>?,
    val overdueInfo: OverdueInfoViewData,
    val productsInfo: ProductsInfoViewData
)
