package lib.dehaat.ledger.presentation.model.revamp.invoice

data class InvoiceDetailsViewData(
    val creditNotes: List<CreditNoteViewData>,
    val productsInfo: ProductsInfoViewDataV2,
    val summary: SummaryViewDataV2
)
