package lib.dehaat.ledger.framework.model.revamp.invoicedetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InvoiceDataV2(
    @Json(name = "credit_notes")
    val creditNotes: List<CreditNote>,
    @Json(name = "products_info")
    val productsInfo: ProductsInfo,
    @Json(name = "summary")
    val summary: Summary
)
