package lib.dehaat.ledger.framework.model.revamp.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditSummaryDataV2(
    @Json(name = "credit")
    val credit: CreditV2
)
