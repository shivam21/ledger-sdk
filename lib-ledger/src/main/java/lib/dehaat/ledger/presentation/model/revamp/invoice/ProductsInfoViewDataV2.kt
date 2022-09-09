package lib.dehaat.ledger.presentation.model.revamp.invoice

data class ProductsInfoViewDataV2(
    val count: Int,
    val discount: String?,
    val gst: String?,
    val productList: List<ProductViewDataV2>?,
    val itemTotal: String,
    val subTotal: String
)
