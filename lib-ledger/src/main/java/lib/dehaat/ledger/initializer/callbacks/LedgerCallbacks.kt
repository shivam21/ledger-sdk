package lib.dehaat.ledger.initializer.callbacks

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import lib.dehaat.ledger.presentation.model.creditsummary.CreditSummaryViewData
import lib.dehaat.ledger.presentation.model.invoicedownload.InvoiceDownloadData

interface LedgerCallbacks {
    fun onClickPayNow(creditSummaryViewData: CreditSummaryViewData?)
    fun onClickDownloadInvoice(invoiceDownloadData: InvoiceDownloadData)
    fun onPaymentOptionsClick(
        creditSummaryViewData: CreditSummaryViewData?,
        resultLauncher: ActivityResultLauncher<Intent?>
    )
}

data class LedgerCallBack(
    val onClickPayNow: PayNowClick,
    val onClickDownloadInvoice: DownloadInvoiceClick,
    val onPaymentOptionsClick: PaymentOptionsClick,
    val downloadInvoiceIntent: DownloadInvoiceIntent
)
typealias PayNowClick = (creditSummaryViewData: CreditSummaryViewData?) -> Unit
typealias DownloadInvoiceIntent = (Context, String) -> PendingIntent?
typealias DownloadInvoiceClick = (invoiceDownloadData: InvoiceDownloadData) -> Unit

typealias PaymentOptionsClick = (
    creditSummaryViewData: CreditSummaryViewData?,
    resultLauncher: ActivityResultLauncher<Intent?>
) -> Unit
