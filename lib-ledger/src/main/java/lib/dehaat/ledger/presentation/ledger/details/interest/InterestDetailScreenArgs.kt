package lib.dehaat.ledger.presentation.ledger.details.interest

import android.os.Bundle
import lib.dehaat.ledger.presentation.ledger.details.interest.ui.InterestViewData
import lib.dehaat.ledger.presentation.model.revamp.SummaryViewData
import lib.dehaat.ledger.presentation.model.revamp.transactions.TransactionViewDataV2

class InterestDetailScreenArgs(
    private val bundle: Bundle
) {

    fun getArgs() = InterestViewData(
        amount = bundle.getString(KEY_INTEREST_AMOUNT) ?: "",
        interest = bundle.getString(KEY_INTEREST) ?: "",
        totalPayment = bundle.getString(KEY_TOTAL_PAYMENT) ?: ""
    )

    companion object {
        private const val KEY_INTEREST_AMOUNT = "KEY_INTEREST_AMOUNT"
        private const val KEY_INTEREST = "KEY_INTEREST"
        private const val KEY_TOTAL_PAYMENT = "KEY_TOTAL_PAYMENT"

        fun getBundle(
            transaction: TransactionViewDataV2?,
            summaryViewData: SummaryViewData?
        ) = Bundle().apply {
            transaction?.let {
                putString(KEY_INTEREST_AMOUNT, it.amount)
                putString(KEY_INTEREST, summaryViewData?.totalInterestOutstanding.orEmpty())
                putString(KEY_TOTAL_PAYMENT, summaryViewData?.totalInterestPaid.orEmpty())
            }
        }
    }
}
