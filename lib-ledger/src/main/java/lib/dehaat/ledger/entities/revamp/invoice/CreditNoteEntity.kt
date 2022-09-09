package lib.dehaat.ledger.entities.revamp.invoice

data class CreditNoteEntity(
    val creditNoteAmount: String,
    val creditNoteDate: Long,
    val creditNoteType: String,
    val ledgerId: Int
)
