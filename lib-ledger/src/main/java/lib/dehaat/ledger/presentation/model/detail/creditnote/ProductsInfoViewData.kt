package lib.dehaat.ledger.presentation.model.detail.creditnote

data class ProductsInfoViewData(
    val count: Int,
    val gst: String,
    val itemTotal: String,
    val subTotal: String,
    val productList: List<ProductViewData>?
)
