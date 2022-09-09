package lib.dehaat.ledger.framework.model.revamp.invoicedetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditNote(
    @Json(name = "credit_note_amount")
    val creditNoteAmount: String,
    @Json(name = "credit_note_date")
    val creditNoteDate: Long,
    @Json(name = "credit_note_type")
    val creditNoteType: String,
    @Json(name = "ledger_id")
    val ledgerId: Int
)
