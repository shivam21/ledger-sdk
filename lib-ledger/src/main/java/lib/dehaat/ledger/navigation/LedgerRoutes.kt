package lib.dehaat.ledger.navigation

sealed class LedgerRoutes(val screen: String) {
    object LedgerDetailScreen :
        LedgerRoutes("ledger_detail_screen")

    object LedgerInvoiceDetailScreen :
        LedgerRoutes("ledger_invoice_detail_screen")

    object LedgerCreditNoteDetailScreen :
        LedgerRoutes("ledger_credit_note_detail_screen")

    object LedgerPaymentDetailScreen :
        LedgerRoutes("ledger_payment_detail_screen")

    object RevampLedgerScreen : LedgerRoutes("revamp_ledger_screen")

    object TotalOutstandingDetailScreen : LedgerRoutes("total_outstanding_detail_screen")

    object InvoiceListScreen: LedgerRoutes("invoice_list_screen")

    object TotalAvailableCreditLimitScreen :
        LedgerRoutes("total_available_credit_limit_detail_screen")

    object RevampLedgerInvoiceDetailScreen :
        LedgerRoutes("revamp_ledger_invoice_detail_screen")

    object RevampLedgerCreditNoteDetailScreen :
        LedgerRoutes("revamp_ledger_credit_note_detail_screen")

    object RevampLedgerPaymentDetailScreen :
        LedgerRoutes("revamp_ledger_payment_detail_screen")

    object RevampLedgerWeeklyInterestDetailScreen :
        LedgerRoutes("revamp_ledger_weekly_interest_detail_screen")
}
