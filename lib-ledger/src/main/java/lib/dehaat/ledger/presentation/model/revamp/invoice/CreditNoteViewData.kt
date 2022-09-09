package lib.dehaat.ledger.presentation.model.revamp.invoice

data class CreditNoteViewData(
    val creditNoteAmount: String,
    val creditNoteDate: Long,
    val creditNoteType: String,
    val ledgerId: Int
)
