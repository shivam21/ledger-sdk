package lib.dehaat.ledger.framework.network

import lib.dehaat.ledger.framework.model.abs.ResponseABSTransactions
import lib.dehaat.ledger.framework.model.creditlines.ResponseCreditLines
import lib.dehaat.ledger.framework.model.creditsummary.ResponseCreditSummary
import lib.dehaat.ledger.framework.model.detail.creditnote.ResponseCreditNoteDetail
import lib.dehaat.ledger.framework.model.detail.invoice.ResponseInvoiceDetail
import lib.dehaat.ledger.framework.model.detail.invoice.invoicedownload.ResponseDownloadInvoice
import lib.dehaat.ledger.framework.model.detail.payment.ResponsePaymentDetail
import lib.dehaat.ledger.framework.model.revamp.creditnote.ResponseCreditNoteDetails
import lib.dehaat.ledger.framework.model.revamp.creditsummary.ResponseCreditSummaryV2
import lib.dehaat.ledger.framework.model.revamp.invoicedetails.ResponseInvoiceDetails
import lib.dehaat.ledger.framework.model.revamp.invoicelist.ResponseInvoiceList
import lib.dehaat.ledger.framework.model.revamp.transactions.ResponseTransaction
import lib.dehaat.ledger.framework.model.transactions.ResponseTransactions
import lib.dehaat.ledger.framework.model.transactionsummary.ResponseTransactionSummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LedgerAPIService {

    @GET("/finance/accounting/credit-summary")
    suspend fun getCreditSummary(
        @Query("partner_id") partnerId: String
    ): Response<ResponseCreditSummary>

    @GET("/finance/accounting/credit-summary/v2")
    suspend fun getV2CreditSummary(
        @Query("partner_id") partnerId: String
    ): Response<ResponseCreditSummaryV2>

    @GET("/finance/accounting/transactions-summary")
    suspend fun getTransactionSummary(
        @Query("partner_id") partnerId: String,
        @Query("from_date") fromDate: Long?,
        @Query("to_date") toDate: Long?
    ): Response<ResponseTransactionSummary>

    @GET("/finance/accounting/transactions-summary/v2")
    suspend fun getTransactionSummaryV2(
        @Query("partner_id") partnerId: String,
        @Query("from_date") fromDate: Long?,
        @Query("to_date") toDate: Long?
    ): Response<ResponseTransactionSummary>

    @GET("/finance/accounting/transactions")
    suspend fun getTransactions(
        @Query("partner_id") partnerId: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("only_penalty_invoices") onlyPenaltyInvoices: Boolean,
        @Query("from_date") fromDate: Long?,
        @Query("to_date") toDate: Long?,
    ): Response<ResponseTransactions>

    @GET("/finance/accounting/transactions/v2")
    suspend fun getTransactionsV2(
        @Query("partner_id") partnerId: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("from_date") fromDate: Long?,
        @Query("to_date") toDate: Long?,
    ): Response<ResponseTransaction>

    @GET("/finance/accounting/credit-lines")
    suspend fun getCreditLines(
        @Query("partner_id") partnerId: String
    ): Response<ResponseCreditLines>

    @GET("/finance/invoice")
    suspend fun getInvoiceDetail(
        @Query("ledger_id") ledgerId: String
    ): Response<ResponseInvoiceDetail>

    @GET("/finance/invoice/v2")
    suspend fun getInvoiceDetails(
        @Query("ledger_id") ledgerId: String
    ): Response<ResponseInvoiceDetails>

    @GET("/finance/payment")
    suspend fun getPaymentDetail(
        @Query("ledger_id") ledgerId: String
    ): Response<ResponsePaymentDetail>

    @GET("/finance/credit-note")
    suspend fun getCreditNoteDetail(
        @Query("ledger_id") ledgerId: String
    ): Response<ResponseCreditNoteDetail>

    @GET("/finance/credit-note/v2")
    suspend fun getCreditNoteDetailV2(
        @Query("ledger_id") ledgerId: String
    ): Response<ResponseCreditNoteDetails>

    @GET("/stock-management/v1/invoice-download")
    suspend fun downloadInvoice(
        @Query("invoice_id") identityId: String,
        @Query("source") source: String
    ): Response<ResponseDownloadInvoice>

    @GET("/finance/invoice/v2")
    suspend fun getInvoiceList(
        @Query("partner_id") partnerId: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("filter") filter: String
    ): Response<ResponseInvoiceList>

    @GET("/finance/accounting/abs-transactions")
    suspend fun getABSTransactions(
        @Query("partner_id") partnerId: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<ResponseABSTransactions>
}
