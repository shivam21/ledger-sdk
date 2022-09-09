package lib.dehaat.ledger.entities.revamp.transaction

data class TransactionEntityV2(
    val amount: String,
    val creditNoteReason: String?,
    val date: Long,
    val erpId: String?,
    val interestEndDate: Long?,
    val interestStartDate: Long?,
    val ledgerId: String,
    val locusId: Int?,
    val partnerId: String,
    val paymentMode: String?,
    val source: String,
    val sourceNo: String?,
    val type: String
)
