package lib.dehaat.ledger.data

import javax.inject.Inject
import lib.dehaat.ledger.data.source.ILedgerDataSource
import lib.dehaat.ledger.domain.ILedgerRepository

class LedgerRepository @Inject constructor(private val networkSource: ILedgerDataSource) :
    ILedgerRepository {

    override suspend fun getCreditSummary(partnerId: String) =
        networkSource.getCreditSummary(partnerId)

    override suspend fun getCreditSummaryV2(
        partnerId: String
    ) = networkSource.getCreditSummaryV2(partnerId)

    override suspend fun getTransactionSummary(
        partnerId: String,
        fromDate: Long?,
        toDate: Long?
    ) = networkSource.getTransactionSummary(partnerId, fromDate, toDate)

    override suspend fun getTransactionSummaryV2(
        partnerId: String,
        fromDate: Long?,
        toDate: Long?
    ) = networkSource.getTransactionSummaryV2(partnerId, fromDate, toDate)

    override suspend fun getTransactions(
        partnerId: String,
        limit: Int,
        offset: Int,
        onlyPenaltyInvoices: Boolean,
        fromDate: Long?,
        toDate: Long?,
    ) = networkSource.getTransactions(
        partnerId,
        limit,
        offset,
        onlyPenaltyInvoices,
        fromDate,
        toDate
    )

    override suspend fun getTransactionsV2(
        partnerId: String,
        limit: Int,
        offset: Int,
        fromDate: Long?,
        toDate: Long?
    ) = networkSource.getTransactionsV2(
        partnerId,
        limit,
        offset,
        fromDate,
        toDate
    )

    override suspend fun getCreditLines(
        partnerId: String
    ) = networkSource.getCreditLines(
        partnerId
    )

    override suspend fun getInvoiceDetail(
        ledgerId: String
    ) = networkSource.getInvoiceDetail(
        ledgerId
    )

    override suspend fun getInvoiceDetails(
        ledgerId: String
    ) = networkSource.getInvoiceDetails(
        ledgerId
    )

    override suspend fun getInvoiceDownload(
        identityId: String,
        source: String
    ) = networkSource.getInvoiceDownload(
        identityId,
        source
    )

    override suspend fun getPaymentDetail(
        ledgerId: String
    ) = networkSource.getPaymentDetail(
        ledgerId
    )

    override suspend fun getCreditNoteDetail(
        ledgerId: String
    ) = networkSource.getCreditNoteDetail(
        ledgerId
    )

    override suspend fun getCreditNoteDetails(
        ledgerId: String
    ) = networkSource.getCreditNoteDetailV2(
        ledgerId
    )

    override suspend fun getInterestApproachedInvoices(
        partnerId: String,
        limit: Int,
        offset: Int,
        isInterestApproached: Boolean
    ) = networkSource.getInvoices(
        partnerId,
        limit,
        offset,
        isInterestApproached
    )

    override suspend fun getABSTransactions(
        partnerId: String,
        limit: Int,
        offset: Int
    ) = networkSource.getABSTransactions(partnerId, limit, offset)
}
