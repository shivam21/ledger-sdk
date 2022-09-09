package lib.dehaat.ledger.entities.revamp.invoicelist

data class InvoiceListEntity(
    val amount: String,
    val date: Long,
    val interestStartDate: Long,
    val interestFreePeriodEndDate: Long,
    val ledgerId: String,
    val locusId: Int?,
    val outstandingAmount: String,
    val partnerId: String,
    val source: String,
    val type: String,
    val interestDays: Int?
)
