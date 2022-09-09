package lib.dehaat.ledger.entities.detail.creditnote

data class ProductsInfoEntity(
    val count: Int,
    val gst: String,
    val discount: String?,
    val itemTotal: String,
    val subTotal: String,
    val productList: List<ProductEntity>?
)
