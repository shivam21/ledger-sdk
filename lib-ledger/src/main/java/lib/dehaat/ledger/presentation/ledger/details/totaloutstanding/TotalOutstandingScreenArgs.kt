package lib.dehaat.ledger.presentation.ledger.details.totaloutstanding

import android.os.Bundle
import lib.dehaat.ledger.presentation.ledger.revamp.state.credits.outstandingcreditlimit.OutstandingCreditLimitViewState

class TotalOutstandingScreenArgs constructor(
    val bundle: Bundle
) {
    val viewState = OutstandingCreditLimitViewState(
        totalOutstandingAmount = bundle.getString(TOTAL_OUTSTANDING_AMOUNT, ""),
        totalPurchaseAmount = bundle.getString(TOTAL_PURCHASE_AMOUNT, ""),
        interestTillDate = bundle.getString(INTEREST_TILL_DATE, ""),
        paymentAmountTillDate = bundle.getString(PAYMENT_AMOUNT_TILL_DATE, ""),
        purchaseAmountTillDate = bundle.getString(PURCHASE_AMOUNT_TILL_DATE, ""),
        creditNoteAmountTillDate = bundle.getString(CREDIT_NOTE_AMOUNT_TILL_DATE, ""),
    )

    companion object {
        private const val TOTAL_OUTSTANDING_AMOUNT = "TOTAL_OUTSTANDING_AMOUNT"
        private const val TOTAL_PURCHASE_AMOUNT = "TOTAL_PURCHASE_AMOUNT"
        private const val INTEREST_TILL_DATE = "INTEREST_TILL_DATE"
        private const val PAYMENT_AMOUNT_TILL_DATE = "PAYMENT_AMOUNT_TILL_DATE"
        private const val PURCHASE_AMOUNT_TILL_DATE = "PURCHASE_AMOUNT_TILL_DATE"
        private const val CREDIT_NOTE_AMOUNT_TILL_DATE = "CREDIT_NOTE_AMOUNT_TILL_DATE"

        fun getBundle(viewState: OutstandingCreditLimitViewState?) = Bundle().apply {
            viewState?.let {
                putString(TOTAL_OUTSTANDING_AMOUNT, it.totalOutstandingAmount)
                putString(TOTAL_PURCHASE_AMOUNT, it.totalPurchaseAmount)
                putString(INTEREST_TILL_DATE, it.interestTillDate)
                putString(PAYMENT_AMOUNT_TILL_DATE, it.paymentAmountTillDate)
                putString(PURCHASE_AMOUNT_TILL_DATE, it.purchaseAmountTillDate)
                putString(CREDIT_NOTE_AMOUNT_TILL_DATE, it.creditNoteAmountTillDate)
            }
        }
    }
}
