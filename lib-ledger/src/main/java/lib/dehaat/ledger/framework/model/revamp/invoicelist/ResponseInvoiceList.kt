package lib.dehaat.ledger.framework.model.revamp.invoicelist


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseInvoiceList(
    @Json(name = "data")
    val data: List<InterestInvoice>?
)
