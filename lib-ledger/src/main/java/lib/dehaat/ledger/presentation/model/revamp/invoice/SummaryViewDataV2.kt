package lib.dehaat.ledger.presentation.model.revamp.invoice

data class SummaryViewDataV2(
    val interestBeingCharged: Boolean?,
    val interestDays: Int?,
    val interestStartDate: Long?,
    val invoiceAmount: String?,
    val invoiceDate: Long,
    val invoiceId: String,
    val processingFee: String?,
    val totalOutstandingAmount: String?
)
