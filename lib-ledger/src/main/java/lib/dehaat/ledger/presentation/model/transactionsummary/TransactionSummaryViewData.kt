package lib.dehaat.ledger.presentation.model.transactionsummary

import lib.dehaat.ledger.presentation.model.revamp.transactionsummary.ABSViewData

data class TransactionSummaryViewData(
    val purchaseAmount: String,
    val paymentAmount: String,
    val abs: ABSViewData?
)
