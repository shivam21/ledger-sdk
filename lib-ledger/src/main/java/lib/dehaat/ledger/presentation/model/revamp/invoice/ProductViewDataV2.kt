package lib.dehaat.ledger.presentation.model.revamp.invoice

data class ProductViewDataV2(
    val fname: String?,
    val name: String,
    val priceTotal: String,
    val priceTotalDiscexcl: String,
    val quantity: Int?
)
