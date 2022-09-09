package lib.dehaat.ledger.entities.revamp.creditsummary

data class CreditSummaryEntityV2(
    val bufferLimit: String,
    val creditNoteAmountTillDate: String,
    val externalFinancierSupported: Boolean,
    val interestTillDate: String,
    val minInterestAmountDue: String,
    val minInterestOutstandingDate: Long,
    val minOutstandingAmountDue: String,
    val paymentAmountTillDate: String,
    val permanentCreditLimit: String,
    val purchaseAmountTillDate: String,
    val totalAvailableCreditLimit: String,
    val totalCreditLimit: String,
    val totalOutstandingAmount: String,
    val totalPurchaseAmount: String,
    val undeliveredInvoiceAmount: String,
    val totalInterestOutstanding: String,
    val totalInterestPaid: String
)
