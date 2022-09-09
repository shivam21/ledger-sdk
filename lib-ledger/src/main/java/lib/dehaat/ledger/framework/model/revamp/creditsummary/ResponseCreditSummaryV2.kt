package lib.dehaat.ledger.framework.model.revamp.creditsummary

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseCreditSummaryV2(
    @Json(name = "data")
    val data: CreditSummaryDataV2
)
