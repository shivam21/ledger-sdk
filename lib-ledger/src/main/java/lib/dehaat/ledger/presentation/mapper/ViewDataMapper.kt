package lib.dehaat.ledger.presentation.mapper

import javax.inject.Inject
import lib.dehaat.ledger.entities.revamp.creditsummary.CreditSummaryEntityV2
import lib.dehaat.ledger.entities.transactionsummary.ABSEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.initializer.toDateMonthName
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.availablecreditlimit.AvailableCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.outstandingcreditlimit.OutstandingCreditLimitViewState
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.ABSViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.TransactionSummaryViewData
import lib.dehaat.ledger.util.getRoundedAmountInRupees
import lib.dehaat.ledger.presentation.ledger.revamp.state.outstandingcalculation.OutstandingCalculationUiState
import lib.dehaat.ledger.util.getAmountInRupees
import lib.dehaat.ledger.util.toDoubleOrZero

class ViewDataMapper @Inject constructor() {

    fun toCreditSummaryViewData(entity: CreditSummaryEntityV2) = with(entity) {
        SummaryViewData(
            bufferLimit = bufferLimit,
            creditNoteAmountTillDate = creditNoteAmountTillDate,
            externalFinancierSupported = externalFinancierSupported,
            interestTillDate = interestTillDate,
            minInterestAmountDue = minInterestAmountDue,
            minInterestOutstandingDate = minInterestOutstandingDate,
            minOutstandingAmountDue = minOutstandingAmountDue,
            paymentAmountTillDate = paymentAmountTillDate,
            permanentCreditLimit = permanentCreditLimit,
            purchaseAmountTillDate = purchaseAmountTillDate,
            totalAvailableCreditLimit = totalAvailableCreditLimit,
            totalCreditLimit = totalCreditLimit,
            totalOutstandingAmount = totalOutstandingAmount,
            totalPurchaseAmount = totalPurchaseAmount,
            undeliveredInvoiceAmount = undeliveredInvoiceAmount,
            totalInterestOutstanding = totalInterestOutstanding,
            totalInterestPaid = totalInterestPaid,
            minimumRepaymentAmount = minimumRepaymentAmount,
            hideMinimumRepaymentSection = minimumRepaymentAmount.toDoubleOrZero() <= 0 || repaymentDate == null,
            isOrderingBlocked = minimumRepaymentAmount.toDoubleOrZero() > 0 && overdueAmount.toDoubleOrZero() > overdueCreditLimit.toDoubleOrZero(),
            repaymentDate = repaymentDate.toDateMonthName(),
            showToolTipInformation = overdueAmount.toDoubleOrZero() > 0
        )
    }

    fun toAvailableCreditLimitViewState(entity: CreditSummaryEntityV2) = with(entity) {
        AvailableCreditLimitViewState(
            totalAvailableCreditLimit = totalAvailableCreditLimit,
            totalCreditLimit = totalCreditLimit,
            outstandingAndDeliveredAmount = (totalOutstandingAmount.toDouble() + undeliveredInvoiceAmount.toDouble()).toString(),
            permanentCreditLimit = permanentCreditLimit,
            bufferLimit = bufferLimit,
            totalLimit = (permanentCreditLimit.toDouble() + bufferLimit.toDouble()).toString()
        )
    }

    fun toOutstandingCreditLimitViewState(entity: CreditSummaryEntityV2) = with(entity) {
        OutstandingCreditLimitViewState(
            totalOutstandingAmount,
            totalPurchaseAmount,
            interestTillDate,
            paymentAmountTillDate,
            purchaseAmountTillDate,
            creditNoteAmountTillDate
        )
    }

    fun toTransactionSummaryViewData(
        entity: TransactionSummaryEntity
    ) = TransactionSummaryViewData(
        purchaseAmount = entity.purchaseAmount.getRoundedAmountInRupees(),
        paymentAmount = entity.paymentAmount.getRoundedAmountInRupees(),
        interestAmount = entity.interestAmount.getRoundedAmountInRupees(),
        abs = toABSViewData(entity.abs)
    )

    private fun toABSViewData(abs: ABSEntity?) = abs?.run {
        ABSViewData(amount, lastMoveScheme, showBanner)
    }

    fun toOutstandingCalculationViewData(entity: TransactionSummaryEntity) = with(entity) {
        OutstandingCalculationUiState(
            totalOutstanding = (purchaseAmount.toDoubleOrZero() - netPaymentAmount.toDoubleOrZero()).toString()
                .getAmountInRupees(),
            totalPurchase = purchaseAmount.getAmountInRupees(),
            totalPayment = netPaymentAmount.getAmountInRupees(),
            totalInvoiceAmount = totalInvoiceAmount.getAmountInRupees(),
            totalCreditNoteAmount = creditNoteAmount.getAmountInRupees(),
            outstandingInterestAmount = interestOutstanding.getAmountInRupees(),
            paidInterestAmount = interestPaid.getAmountInRupees(),
            creditNoteAmount = totalInterestRefundAmount.getAmountInRupees(),
            totalDebitNoteAmount = debitNodeAmount.getAmountInRupees(),
            paidAmount = paymentAmount.getAmountInRupees(),
            paidRefund = debitEntryAmount.getAmountInRupees(),
            totalPaid = netPaymentAmount.getAmountInRupees()
        )
    }
}
