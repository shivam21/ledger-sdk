package lib.dehaat.ledger.entities.revamp.invoice

data class ProductEntityV2(
    val fname: String?,
    val name: String,
    val priceTotal: String,
    val priceTotalDiscexcl: String,
    val quantity: Int
)
