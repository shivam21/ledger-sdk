package lib.dehaat.ledger.framework.model.revamp.invoicedetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Summary(
    @Json(name = "interest_being_charged")
    val interestBeingCharged: Boolean?,
    @Json(name = "interest_days")
    val interestDays: Int?,
    @Json(name = "interest_start_date")
    val interestStartDate: Long?,
    @Json(name = "invoice_amount")
    val invoiceAmount: String?,
    @Json(name = "invoice_date")
    val invoiceDate: Long,
    @Json(name = "invoice_id")
    val invoiceId: String,
    @Json(name = "processing_fee")
    val processingFee: String?,
    @Json(name = "total_outstanding_amount")
    val totalOutstandingAmount: String?
)
