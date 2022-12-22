package lib.dehaat.ledger.util

import com.dehaat.androidbase.helper.tryCatchWithReturn
import com.dehaat.androidbase.utils.NumberUtilities
import lib.dehaat.ledger.initializer.LedgerSDK
import lib.dehaat.ledger.initializer.formatDecimal
import java.text.NumberFormat
import java.util.*

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

fun String?.getRoundedAmountInRupees(): String {
    val value = this?.toDoubleOrNull()
    val isNegativeValue = value?.let { it < 0 } ?: false
    if (isNegativeValue) {
        value?.let {
            val amount = it * -1
            return String.format("%s%s", "- ₹", formatDecimal(amount, 0))
        }
    }
    return String.format("%s%s", "₹", formatDecimal(value, 0))
}

private fun String.getAmountWithoutTrailingZeroes() = if (this.endsWith(".00")) {
    this.substringBeforeLast(".00")
} else {
    this
}

fun String?.getAmountInRupeesOrDash(): String = this?.let {
    it.getAmountInRupees()
} ?: "-"

fun String.formatAmount()= tryCatchWithReturn(this){
    val formatter = NumberFormat.getNumberInstance(Locale(LedgerSDK.locale, "in"))
    formatter.maximumFractionDigits = 2
    return formatter.format(toDouble())
}
