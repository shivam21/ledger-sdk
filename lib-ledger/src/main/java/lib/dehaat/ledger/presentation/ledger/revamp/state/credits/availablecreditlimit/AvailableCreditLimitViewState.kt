package lib.dehaat.ledger.presentation.ledger.revamp.state.credits.availablecreditlimit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AvailableCreditLimitViewState(
    val totalAvailableCreditLimit: String,
    val totalCreditLimit: String,
    val outstandingAndDeliveredAmount: String,
    val permanentCreditLimit: String,
    val bufferLimit: String,
    val totalLimit: String
) : Parcelable
