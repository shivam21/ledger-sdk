package lib.dehaat.ledger.entities.revamp.creditnote

import lib.dehaat.ledger.entities.revamp.invoice.ProductsInfoEntityV2

data class CreditNoteDetailsEntity(
    val productsInfo: ProductsInfoEntityV2,
    val summary: SummaryEntityV2
)
