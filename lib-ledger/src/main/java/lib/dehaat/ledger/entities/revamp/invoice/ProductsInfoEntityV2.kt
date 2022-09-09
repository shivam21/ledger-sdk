package lib.dehaat.ledger.entities.revamp.invoice

data class ProductsInfoEntityV2(
    val count: Int,
    val discount: String?,
    val gst: String?,
    val productList: List<ProductEntityV2>,
    val itemTotal: String,
    val subTotal: String
)
