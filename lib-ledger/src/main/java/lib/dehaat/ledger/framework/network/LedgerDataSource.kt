package lib.dehaat.ledger.framework.network

import com.cleanarch.base.common.ApiExtraInfo
import com.cleanarch.base.entity.result.api.APIResultEntity
import com.dehaat.androidbase.coroutine.IDispatchers
import com.dehaat.androidbase.network.api.makeAPICall
import javax.inject.Inject
import lib.dehaat.ledger.data.source.ILedgerDataSource
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.framework.mapper.LedgerFrameworkMapper
import lib.dehaat.ledger.presentation.LedgerConstants.API_REQUEST_TRACE_ID
import lib.dehaat.ledger.presentation.LedgerConstants.IB_REQUEST_IDENTIFIER
import retrofit2.Response

class LedgerDataSource @Inject constructor(
    private val dispatcher: IDispatchers,
    private val apiService: LedgerAPIService,
    private val mapper: LedgerFrameworkMapper
) : ILedgerDataSource {

    override suspend fun getCreditSummary(partnerId: String) = callAPI(
        dispatcher,
        { apiService.getCreditSummary(partnerId = partnerId) }) {
        it?.data?.let { data -> mapper.toCreditSummaryDataEntity(data) }
    }

    override suspend fun getCreditSummaryV2(
        partnerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getV2CreditSummary(partnerId) }
    ) {
        it?.data?.credit?.let { data -> mapper.toCreditSummaryEntity(data) }
    }

    override suspend fun getTransactionSummary(
        partnerId: String,
        fromDate: Long?,
        toDate: Long?
    ): APIResultEntity<TransactionSummaryEntity?> = callAPI(
        dispatcher,
        { apiService.getTransactionSummary(partnerId, fromDate, toDate) }
    ) {
        it?.transactionDetailData?.let { data -> mapper.toTransactionSummaryDataEntity(data) }
    }

    override suspend fun getTransactionSummaryV2(
        partnerId: String,
        fromDate: Long?,
        toDate: Long?
    ): APIResultEntity<TransactionSummaryEntity?> = callAPI(
        dispatcher,
        { apiService.getTransactionSummaryV2(partnerId, fromDate, toDate) }
    ) {
        it?.transactionDetailData?.let { data -> mapper.toTransactionSummaryDataEntity(data) }
    }

    override suspend fun getTransactions(
        partnerId: String,
        limit: Int,
        offset: Int,
        onlyPenaltyInvoices: Boolean,
        fromDate: Long?,
        toDate: Long?,
    ) = callAPI(
        dispatcher,
        {
            apiService.getTransactions(
                partnerId = partnerId,
                fromDate = fromDate,
                toDate = toDate,
                onlyPenaltyInvoices = onlyPenaltyInvoices,
                limit = limit,
                offset = offset
            )
        }) {
        it?.transactionsData?.let { data -> mapper.toTransactionsDataEntity(data) } ?: emptyList()
    }

    override suspend fun getTransactionsV2(
        partnerId: String,
        limit: Int,
        offset: Int,
        fromDate: Long?,
        toDate: Long?
    ) = callAPI(
        dispatcher,
        {
            apiService.getTransactionsV2(
                partnerId = partnerId,
                fromDate = fromDate,
                toDate = toDate,
                limit = limit,
                offset = offset
            )
        }
    ) {
        it?.data?.let { data -> mapper.toTransactionsEntity(data) } ?: emptyList()
    }

    override suspend fun getCreditLines(
        partnerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getCreditLines(partnerId = partnerId) }) {
        it?.creditLineData?.let { data -> mapper.toCreditLineDataEntity(data) } ?: emptyList()
    }

    override suspend fun getInvoiceDetail(
        ledgerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getInvoiceDetail(ledgerId) }
    ) {
        it?.invoiceDetailData?.let { data -> mapper.toInvoiceDetailDataEntity(data) }
    }

    override suspend fun getInvoiceDetails(
        ledgerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getInvoiceDetails(ledgerId) }
    ) {
        it?.data?.let { data -> mapper.toInvoiceDetailEntity(data) }
    }

    override suspend fun getInvoiceDownload(
        identityId: String,
        source: String
    ) = callAPI(
        dispatcher,
        { apiService.downloadInvoice(identityId, source) }
    ) {
        it?.downloadInvoiceData?.let { data -> mapper.toInvoiceDownloadDataEntity(data) }
    }

    override suspend fun getPaymentDetail(
        ledgerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getPaymentDetail(ledgerId) }
    ) {
        it?.paymentDetailData?.let { data -> mapper.toPaymentDetailDataEntity(data) }
    }

    override suspend fun getCreditNoteDetail(
        ledgerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getCreditNoteDetail(ledgerId) }
    ) {
        it?.creditNoteDetailData?.let { data -> mapper.toCreditNoteDetailDataEntity(data) }
    }

    override suspend fun getCreditNoteDetailV2(
        ledgerId: String
    ) = callAPI(
        dispatcher,
        { apiService.getCreditNoteDetailV2(ledgerId) }
    ) {
        it?.data?.let { data -> mapper.toCreditNoteDetailsEntity(data) }
    }

    override suspend fun getInvoices(
        partnerId: String,
        limit: Int,
        offset: Int,
        isInterestApproached: Boolean
    ) = callAPI(
        dispatcher,
        {
            apiService.getInvoiceList(
                partnerId, limit, offset, if (isInterestApproached) {
                    "interest_approached_invoices"
                } else {
                    "interest_approaching_invoices"
                }
            )
        }
    ) {
        it?.data?.let { data ->
            mapper.toInterestApproachedInvoiceListEntity(data)
        }
    }

    override suspend fun getABSTransactions(partnerId: String, limit: Int, offset: Int) = callAPI(
        dispatcher,
        { apiService.getABSTransactions(partnerId, limit, offset) }
    ) {
        it?.transactions?.let { transactions -> mapper.toABSTransactionEntityList(transactions) }
    }

    private suspend fun <D, C> callAPI(
        dispatchers: IDispatchers,
        apiCall: suspend () -> Response<D>,
        parse: (D?) -> C
    ): APIResultEntity<C> {
        return makeAPICall(
            dispatchers.io,
            { apiCall.invoke() },
            parse
        ) { request, response ->
            ApiExtraInfo().apply {
                put(API_REQUEST_TRACE_ID, request?.header(API_REQUEST_TRACE_ID))
                put(IB_REQUEST_IDENTIFIER, response?.headers()?.get(IB_REQUEST_IDENTIFIER))
            }
        }
    }
}
