package lib.dehaat.ledger.entities.revamp.invoice

data class InvoiceDataEntity(
    val creditNotes: List<CreditNoteEntity>,
    val productsInfo: ProductsInfoEntityV2,
    val summary: SummaryEntityV2
)
