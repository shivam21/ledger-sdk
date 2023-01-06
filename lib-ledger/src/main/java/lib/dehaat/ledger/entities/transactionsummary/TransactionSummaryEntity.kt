package lib.dehaat.ledger.entities.transactionsummary

data class TransactionSummaryEntity(
    val purchaseAmount: String,
    val paymentAmount: String,
    val interestAmount: String?,
    val abs: ABSEntity?
)

data class ABSEntity(
    val amount: Double,
    val lastMoveScheme: String?,
    val showBanner: Boolean
)
