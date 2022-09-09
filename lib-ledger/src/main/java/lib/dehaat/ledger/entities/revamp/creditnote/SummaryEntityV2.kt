package lib.dehaat.ledger.entities.revamp.creditnote

data class SummaryEntityV2(
    val amount: String,
    val invoiceDate: Long,
    val invoiceNumber: String,
    val reason: String,
    val timestamp: Long
)
