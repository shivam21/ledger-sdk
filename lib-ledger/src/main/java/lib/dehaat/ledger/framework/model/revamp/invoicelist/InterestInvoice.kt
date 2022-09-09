package lib.dehaat.ledger.framework.model.revamp.invoicelist


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InterestInvoice(
    @Json(name = "amount")
    val amount: String,
    @Json(name = "date")
    val date: Long,
    @Json(name = "interest_start_date")
    val interestStartDate: Long,
    @Json(name = "interest_free_period_end_date")
    val interestFreePeriodEndDate: Long,
    @Json(name = "ledger_id")
    val ledgerId: String,
    @Json(name = "locus_id")
    val locusId: Int?,
    @Json(name = "outstanding_amount")
    val outstandingAmount: String,
    @Json(name = "partner_id")
    val partnerId: String,
    @Json(name = "source")
    val source: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "interest_days")
    val interestDays: Int?
)
