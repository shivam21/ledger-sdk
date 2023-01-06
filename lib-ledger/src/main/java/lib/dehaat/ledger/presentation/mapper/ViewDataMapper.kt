package lib.dehaat.ledger.presentation.mapper

import lib.dehaat.ledger.entities.revamp.creditsummary.CreditSummaryEntityV2
import lib.dehaat.ledger.entities.transactionsummary.ABSEntity
import lib.dehaat.ledger.entities.transactionsummary.TransactionSummaryEntity
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.availablecreditlimit.AvailableCreditLimitViewState
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.outstandingcreditlimit.OutstandingCreditLimitViewState
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.ABSViewData
import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.TransactionSummaryViewData
import lib.dehaat.ledger.util.getRoundedAmountInRupees
import javax.inject.Inject

class ViewDataMapper @Inject constructor() {

    fun toCreditSummaryViewData(entity: CreditSummaryEntityV2) = with(entity) {
        SummaryViewData(
            bufferLimit,
            creditNoteAmountTillDate,
            externalFinancierSupported,
            interestTillDate,
            minInterestAmountDue,
            minInterestOutstandingDate,
            minOutstandingAmountDue,
            paymentAmountTillDate,
            permanentCreditLimit,
            purchaseAmountTillDate,
            totalAvailableCreditLimit,
            totalCreditLimit,
            totalOutstandingAmount,
            totalPurchaseAmount,
            undeliveredInvoiceAmount,
            totalInterestOutstanding,
            totalInterestPaid,
            minimumRepaymentAmount
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
}
