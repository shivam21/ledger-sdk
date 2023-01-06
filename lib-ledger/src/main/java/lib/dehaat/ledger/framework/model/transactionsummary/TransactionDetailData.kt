package lib.dehaat.ledger.framework.model.transactionsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionDetailData(
    @Json(name = "purchase_amount")
    val purchaseAmount: String,
    @Json(name = "payment_amount")
    val paymentAmount: String,
    @Json(name = "interest_amount")
    val interestAmount: String?,
    @Json(name = "abs")
    val abs: ABSData?
)

@JsonClass(generateAdapter = true)
data class ABSData(
    @Json(name = "amount")
    val amount: Double?,
    @Json(name = "last_move_scheme")
    val lastMoveScheme: String?,
    @Json(name = "show_banner")
    val showBanner: Boolean?
)


