package lib.dehaat.ledger.navigation

import android.os.Bundle

interface DetailPageNavigationCallback {
    fun navigateToInvoiceDetailPage(args: Bundle)

    fun navigateToCreditNoteDetailPage(args: Bundle)

    fun navigateToPaymentDetailPage(args: Bundle)

    fun navigateToOutstandingDetailPage(args: Bundle)

    fun navigateToInvoiceListPage(args: Bundle)

    fun navigateToAvailableCreditLimitDetailPage(args: Bundle)

    fun navigateToRevampInvoiceDetailPage(args: Bundle)

    fun navigateToRevampCreditNoteDetailPage(args: Bundle)

    fun navigateToRevampPaymentDetailPage(args: Bundle)

    fun navigateToRevampWeeklyInterestDetailPage(args: Bundle)

}
