package lib.dehaat.ledger.framework.model.revamp.creditnote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseCreditNoteDetails(
    @Json(name = "data")
    val data: CreditNoteDetailsData
)
