package lib.dehaat.ledger.framework.model.revamp.transactions


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransactionData(
    @Json(name = "transactions")
    val transactions: List<TransactionV2>
)
