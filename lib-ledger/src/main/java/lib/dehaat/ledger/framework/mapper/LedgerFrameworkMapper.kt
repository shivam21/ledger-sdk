package lib.dehaat.ledger.framework.mapper

import lib.dehaat.ledger.entities.creditlines.CreditLineEntity
import lib.dehaat.ledger.entities.creditsummary.CreditEntity
import lib.dehaat.ledger.entities.creditsummary.CreditSummaryEntity
import lib.dehaat.ledger.entities.creditsummary.InfoEntity
import lib.dehaat.ledger.entities.creditsummary.OverdueEntity
import lib.dehaat.ledger.entities.detail.creditnote.CreditNoteDetailEntity
import lib.dehaat.ledger.entities.detail.creditnote.ProductEntity
import lib.dehaat.ledger.entities.detail.creditnote.ProductsInfoEntity
import lib.dehaat.ledger.entities.detail.creditnote.SummaryEntity
import lib.dehaat.ledger.entities.detail.invoice.InvoiceDetailDataEntity
import lib.dehaat.ledger.entities.detail.invoice.LoanEntity
import lib.dehaat.ledger.entities.detail.invoice.invoicedownload.InvoiceDownloadDataEntity
import lib.dehaat.ledger.entities.detail.payment.PaymentDetailEntity
import lib.dehaat.ledger.entities.transactions.TransactionEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.framework.model.creditlines.CreditLine
import lib.dehaat.ledger.framework.model.creditlines.CreditLineData
import lib.dehaat.ledger.framework.model.creditsummary.Credit
import lib.dehaat.ledger.framework.model.creditsummary.CreditSummaryData
import lib.dehaat.ledger.framework.model.creditsummary.Info
import lib.dehaat.ledger.framework.model.creditsummary.Overdue
import lib.dehaat.ledger.framework.model.detail.creditnote.CreditNoteDetailData
import lib.dehaat.ledger.framework.model.detail.creditnote.Product
import lib.dehaat.ledger.framework.model.detail.creditnote.ProductsInfo
import lib.dehaat.ledger.framework.model.detail.creditnote.Summary
import lib.dehaat.ledger.framework.model.detail.invoice.InvoiceDetailData
import lib.dehaat.ledger.framework.model.detail.invoice.Loan
import lib.dehaat.ledger.framework.model.detail.invoice.invoicedownload.DownloadInvoiceData
import lib.dehaat.ledger.framework.model.detail.payment.PaymentDetailData
import lib.dehaat.ledger.framework.model.transactions.Transaction
import lib.dehaat.ledger.framework.model.transactions.TransactionsData
import lib.dehaat.ledger.framework.model.transactionsummary.TransactionDetailData
import javax.inject.Inject

typealias NetworkPaymentDetailSummary = lib.dehaat.ledger.framework.model.detail.payment.Summary
typealias EntityPaymentDetailSummary = lib.dehaat.ledger.entities.detail.payment.SummaryEntity
typealias NetworkInvoiceDetailSummary = lib.dehaat.ledger.framework.model.detail.invoice.Summary
typealias EntityInvoiceDetailSummary = lib.dehaat.ledger.entities.detail.invoice.SummaryEntity
typealias NetworkInvoiceDetailProductsInfo = lib.dehaat.ledger.framework.model.detail.invoice.ProductsInfo
typealias EntityInvoiceDetailProductsInfo = lib.dehaat.ledger.entities.detail.invoice.ProductsInfoEntity
typealias NetworkInvoiceDetailProduct = lib.dehaat.ledger.framework.model.detail.invoice.Product
typealias EntityInvoiceDetailProduct = lib.dehaat.ledger.entities.detail.invoice.ProductEntity

class LedgerFrameworkMapper @Inject constructor() {

    fun toCreditSummaryDataEntity(creditSummaryData: CreditSummaryData) = with(creditSummaryData) {
        CreditSummaryEntity(
            credit = toCreditSummaryCreditEntity(credit),
            overdue = toCreditSummaryOverDueEntity(overdue),
            info = toCreditSummaryInfoEntity(info),
        )
    }

    fun toTransactionSummaryDataEntity(
        transactionDetailData: TransactionDetailData
    ) = with(transactionDetailData) {
        TransactionSummaryEntity(
            purchaseAmount = purchaseAmount,
            paymentAmount = paymentAmount
        )
    }

    fun toCreditLineDataEntity(data: CreditLineData) = data.creditLines.map {
        toCreditLineEntity(it)
    }

    fun toTransactionsDataEntity(data: TransactionsData) = data.transactions.map {
        toTransactionEntity(it)
    }

    fun toCreditNoteDetailDataEntity(data: CreditNoteDetailData) = with(data) {
        CreditNoteDetailEntity(
            summary = getCreditNoteDetailSummaryEntity(summary),
            productsInfo = getCreditNoteDetailProductInfoEntity(productsInfo),
        )
    }

    fun toPaymentDetailDataEntity(data: PaymentDetailData) = with(data) {
        PaymentDetailEntity(
            summary = getPaymentDetailSummaryEntity(summary),
        )
    }

    fun toInvoiceDetailDataEntity(data: InvoiceDetailData) = with(data) {
        InvoiceDetailDataEntity(
            summary = getInvoiceDetailSummaryEntity(summary),
            loans = loans.map { getInvoiceDetailLoanEntity(it) },
            productsInfo = getInvoiceDetailProductInfoEntity(productsInfo),
        )
    }

    fun toInvoiceDownloadDataEntity(data: DownloadInvoiceData) = with(data) {
        InvoiceDownloadDataEntity(
            source = source,
            pdf = pdf,
            fileName = fileName,
            docType = docType
        )
    }

    private fun getPaymentDetailSummaryEntity(data: NetworkPaymentDetailSummary) = with(data) {
        EntityPaymentDetailSummary(
            referenceId = referenceId,
            timestamp = timestamp,
            totalAmount = totalAmount,
            mode = mode,
            principalComponent = principalComponent,
            interestComponent = interestComponent,
            overdueInterestComponent = overdueInterestComponent,
            penaltyComponent = penaltyComponent,
            advanceComponent = advanceComponent,
            paidTo = paidTo,
            belongsToGapl = belongsToGapl
        )
    }

    private fun getInvoiceDetailProductInfoEntity(data: NetworkInvoiceDetailProductsInfo) =
        with(data) {
            EntityInvoiceDetailProductsInfo(
                count = count,
                discount = discount,
                gst = gst,
                itemTotal = itemTotal,
                subTotal = subTotal,
                productList = productList.map {
                    getInvoiceDetailProductEntity(it)
                }
            )
        }

    private fun getInvoiceDetailProductEntity(it: NetworkInvoiceDetailProduct) =
        with(it) {
            EntityInvoiceDetailProduct(
                fname = fname,
                name = name,
                priceTotal = priceTotal,
                priceTotalDiscexcl = priceTotalDiscexcl,
                quantity = quantity
            )
        }

    private fun getInvoiceDetailLoanEntity(data: Loan) = with(data) {
        LoanEntity(
            loanAccountNo = loanAccountNo,
            status = status,
            amount = amount,
            invoiceContributionInLoan = invoiceContributionInLoan,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            disbursalDate = disbursalDate,
            interestFreeEndDate = interestFreeEndDate,
            financier = financier,
            belongsToGapl = belongsToGapl
        )
    }

    private fun getInvoiceDetailSummaryEntity(data: NetworkInvoiceDetailSummary) = with(data) {
        EntityInvoiceDetailSummary(
            amount = amount,
            number = number,
            timestamp = timestamp
        )
    }

    private fun getCreditNoteDetailProductInfoEntity(data: ProductsInfo) = with(data) {
        ProductsInfoEntity(
            count = count,
            gst = gst,
            itemTotal = itemTotal,
            subTotal = subTotal,
            productList = productList.map {
                getCreditNoteDetailProductEntity(it)
            }
        )
    }

    private fun getCreditNoteDetailProductEntity(it: Product) =
        with(it) {
            ProductEntity(
                fname = fname,
                name = name,
                priceTotal = priceTotal,
                priceTotalDiscexcl = priceTotalDiscexcl,
                quantity = quantity
            )
        }

    private fun getCreditNoteDetailSummaryEntity(data: Summary) = with(data) {
        SummaryEntity(
            amount = amount,
            invoiceNumber = invoiceNumber,
            timestamp = timestamp,
            reason = reason
        )
    }

    private fun toTransactionEntity(data: Transaction) = with(data) {
        TransactionEntity(
            ledgerId = ledgerId,
            type = type,
            date = date,
            amount = amount,
            erpId = erpId,
            locusId = locusId,
            creditNoteReason = creditNoteReason,
            paymentMode = paymentMode,
            source = source
        )
    }

    private fun toCreditLineEntity(data: CreditLine) = with(data) {
        CreditLineEntity(
            belongsToGapl = belongsToGapl,
            lenderViewName = lenderViewName,
            creditLimit = creditLimit,
            availableCreditLimit = availableCreditLimit,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount,
            advanceAmount = totalAdvanceAmount
        )
    }

    private fun toCreditSummaryInfoEntity(data: Info) = with(data) {
        InfoEntity(
            totalPurchaseAmount = totalPurchaseAmount,
            totalPaymentAmount = totalPaymentAmount,
            undeliveredInvoiceAmount = undeliveredInvoiceAmount
        )
    }

    private fun toCreditSummaryOverDueEntity(data: Overdue) = with(data) {
        OverdueEntity(
            totalOverdueLimit = totalOverdueLimit,
            totalOverdueAmount = totalOverdueAmount,
            minPaymentAmount = minPaymentAmount,
            minPaymentDueDate = minPaymentDueDate
        )
    }

    private fun toCreditSummaryCreditEntity(data: Credit) = with(data) {
        CreditEntity(
            externalFinancierSupported = externalFinancierSupported,
            totalCreditLimit = totalCreditLimit,
            totalAvailableCreditLimit = totalAvailableCreditLimit,
            totalOutstandingAmount = totalOutstandingAmount,
            principalOutstandingAmount = principalOutstandingAmount,
            interestOutstandingAmount = interestOutstandingAmount,
            overdueInterestOutstandingAmount = overdueInterestOutstandingAmount,
            penaltyOutstandingAmount = penaltyOutstandingAmount
        )
    }
}