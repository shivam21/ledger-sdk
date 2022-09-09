package lib.dehaat.ledger.domain

import com.cleanarch.base.entity.result.api.APIResultEntity
import lib.dehaat.ledger.entities.creditlines.CreditLineEntity
import lib.dehaat.ledger.entities.creditsummary.CreditSummaryEntity
import lib.dehaat.ledger.entities.detail.creditnote.CreditNoteDetailEntity
import lib.dehaat.ledger.entities.detail.invoice.InvoiceDetailDataEntity
import lib.dehaat.ledger.entities.detail.invoice.invoicedownload.InvoiceDownloadDataEntity
import lib.dehaat.ledger.entities.detail.payment.PaymentDetailEntity
import lib.dehaat.ledger.entities.revamp.creditnote.CreditNoteDetailsEntity
import lib.dehaat.ledger.entities.revamp.creditsummary.CreditSummaryEntityV2
import lib.dehaat.ledger.entities.revamp.invoice.InvoiceDataEntity
import lib.dehaat.ledger.entities.revamp.invoicelist.InvoiceListEntity
import lib.dehaat.ledger.entities.revamp.transaction.TransactionEntityV2
import lib.dehaat.ledger.entities.transactions.TransactionEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity

interface ILedgerRepository {

    suspend fun getCreditSummary(partnerId: String): APIResultEntity<CreditSummaryEntity?>

    suspend fun getCreditSummaryV2(partnerId: String): APIResultEntity<CreditSummaryEntityV2?>

    suspend fun getTransactionSummary(
        partnerId: String,
        fromDate: Long?,
        toDate: Long?
    ): APIResultEntity<TransactionSummaryEntity?>

    suspend fun getTransactions(
        partnerId: String,
        limit: Int,
        offset: Int,
        onlyPenaltyInvoices: Boolean,
        fromDate: Long?,
        toDate: Long?,
    ): APIResultEntity<List<TransactionEntity>>

    suspend fun getTransactionsV2(
        partnerId: String,
        limit: Int,
        offset: Int,
        fromDate: Long?,
        toDate: Long?,
    ): APIResultEntity<List<TransactionEntityV2>>

    suspend fun getCreditLines(
        partnerId: String
    ): APIResultEntity<List<CreditLineEntity>>

    suspend fun getInvoiceDetail(
        ledgerId: String
    ): APIResultEntity<InvoiceDetailDataEntity?>

    suspend fun getInvoiceDetails(
        ledgerId: String
    ): APIResultEntity<InvoiceDataEntity?>

    suspend fun getInvoiceDownload(
        identityId: String,
        source: String
    ): APIResultEntity<InvoiceDownloadDataEntity?>

    suspend fun getPaymentDetail(
        ledgerId: String
    ): APIResultEntity<PaymentDetailEntity?>

    suspend fun getCreditNoteDetail(
        ledgerId: String
    ): APIResultEntity<CreditNoteDetailEntity?>

    suspend fun getCreditNoteDetails(
        ledgerId: String
    ): APIResultEntity<CreditNoteDetailsEntity?>

    suspend fun getInterestApproachedInvoices(
        partnerId: String,
        limit: Int,
        offset: Int,
        isInterestApproached: Boolean
    ): APIResultEntity<List<InvoiceListEntity>?>
}
