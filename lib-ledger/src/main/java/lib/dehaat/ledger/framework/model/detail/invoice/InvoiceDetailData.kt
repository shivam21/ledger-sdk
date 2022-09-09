package lib.dehaat.ledger.framework.model.detail.invoice

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InvoiceDetailData(
    @Json(name = "summary")
    val summary: Summary,
    @Json(name = "loans")
    val loans: List<Loan>?,
    @Json(name = "overdue_info")
    val overdueInfo: OverdueInfo?,
    @Json(name = "products_info")
    val productsInfo: ProductsInfo
)
