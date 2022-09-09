package lib.dehaat.ledger.presentation.model.revamp.creditnote

import lib.dehaat.ledger.presentation.model.revamp.invoice.ProductsInfoViewDataV2

data class CreditNoteDetailsViewData(
    val summary: CreditNoteSummaryViewData,
    val productsInfo: ProductsInfoViewDataV2
)
