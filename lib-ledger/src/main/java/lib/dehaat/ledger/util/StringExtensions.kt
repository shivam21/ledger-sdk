package lib.dehaat.ledger.util

import lib.dehaat.ledger.initializer.formatDecimal

fun String?.nullToValue(value: String = "--") = this ?: value

fun String?.getAmountInRupeesWithoutDecimal(): String {
    val value = this?.toDoubleOrNull()
    return String.format("%s%s", "₹", formatDecimal(value, 0))
}

fun String?.getAmountInRupees(): String {
    val value = this?.toDoubleOrNull()
    val isNegativeValue = value?.let { it < 0 } ?: false
    if (isNegativeValue) {
        value?.let {
            val amount = it * -1
            return String.format("%s%s", "- ₹", formatDecimal(amount))
                .getAmountWithoutTrailingZeroes()
        }
    }
    return String.format("%s%s", "₹", formatDecimal(value)).getAmountWithoutTrailingZeroes()
}

private fun String.getAmountWithoutTrailingZeroes() = if (this.endsWith(".00")) {
    this.substringBeforeLast(".00")
} else {
    this
}

fun String?.getAmountInRupeesOrDash(): String = this?.let {
    it.getAmountInRupees()
} ?: "-"
