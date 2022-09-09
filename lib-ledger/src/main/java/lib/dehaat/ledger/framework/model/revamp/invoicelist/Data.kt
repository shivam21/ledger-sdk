package lib.dehaat.ledger.framework.model.revamp.invoicelist


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "interest_approached_invoices")
    val interestApproachedInvoices: List<InterestInvoice>?,
    @Json(name = "interest_approaching_invoices")
    val interestApproachingInvoices: List<InterestInvoice>?
)
