package lib.dehaat.ledger.presentation.model.invoicedownload

data class InvoiceDownloadData(
    var filePath: String = "",
    var partnerId: String = "",
    var invoiceId: String = "",
    var isFailed: Boolean = false,
    var progressData: ProgressData = ProgressData()
)
