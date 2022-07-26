package lib.dehaat.ledger.initializer

import lib.dehaat.ledger.initializer.callbacks.DownloadInvoiceClick
import lib.dehaat.ledger.initializer.callbacks.DownloadInvoiceIntent
import lib.dehaat.ledger.initializer.callbacks.LedgerCallBack
import lib.dehaat.ledger.initializer.themes.AIMSColors
import lib.dehaat.ledger.initializer.themes.DBAColors
import lib.dehaat.ledger.initializer.themes.LedgerColors

sealed class LedgerParentApp(
    val ledgerCallBack: LedgerCallBack,
    val ledgerColors: LedgerColors
) {
    class AIMS(
        downloadInvoiceClick: DownloadInvoiceClick,
        downloadInvoiceIntent: DownloadInvoiceIntent,
        ledgerColors: LedgerColors = AIMSColors()
    ) : LedgerParentApp(
        LedgerCallBack(
            onClickPayNow = {},
            onClickDownloadInvoice = downloadInvoiceClick,
            onPaymentOptionsClick = { _, _ -> },
            downloadInvoiceIntent = downloadInvoiceIntent
        ),
        ledgerColors
    )

    class DBA(
        ledgerCallBack: LedgerCallBack,
        ledgerColors: LedgerColors = DBAColors()
    ) : LedgerParentApp(
        ledgerCallBack,
        ledgerColors
    )
}
