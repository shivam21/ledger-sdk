package lib.dehaat.ledger.entities.detail.invoice

data class InvoiceDetailDataEntity(
    val summary: SummaryEntity,
    val loans: List<LoanEntity>?,
    val overdueInfo: OverdueInfoEntity,
    val productsInfo: ProductsInfoEntity
)
