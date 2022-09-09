package lib.dehaat.ledger.framework.model.revamp.creditnote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Summary(
    @Json(name = "amount")
    val amount: String,
    @Json(name = "invoice_date")
    val invoiceDate: Long,
    @Json(name = "invoice_number")
    val invoiceNumber: String,
    @Json(name = "reason")
    val reason: String,
    @Json(name = "timestamp")
    val timestamp: Long
)
