package lib.dehaat.ledger.presentation.ledger.revamp.state.credits.outstandingcreditlimit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OutstandingCreditLimitViewState(
    val totalOutstandingAmount: String,
    val totalPurchaseAmount: String,
    val interestTillDate: String,
    val paymentAmountTillDate: String,
    val purchaseAmountTillDate: String,
    val creditNoteAmountTillDate: String
): Parcelable
