package lib.dehaat.ledger.framework.model.revamp.invoicedetails

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "fname")
    val fname: String?,
    @Json(name = "name")
    val name: String,
    @Json(name = "price_total")
    val priceTotal: String,
    @Json(name = "price_total_discexcl")
    val priceTotalDiscexcl: String,
    @Json(name = "quantity")
    val quantity: Int
)
