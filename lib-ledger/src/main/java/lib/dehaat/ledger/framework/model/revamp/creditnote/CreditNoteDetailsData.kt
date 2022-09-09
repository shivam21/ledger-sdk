package lib.dehaat.ledger.framework.model.revamp.creditnote


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import lib.dehaat.ledger.framework.model.revamp.invoicedetails.ProductsInfo

@JsonClass(generateAdapter = true)
data class CreditNoteDetailsData(
    @Json(name = "products_info")
    val productsInfo: ProductsInfo,
    @Json(name = "summary")
    val summary: Summary
)
