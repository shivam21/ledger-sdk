package lib.dehaat.ledger.initializer

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import com.dehaat.androidbase.utils.NumberUtilities
import com.dehaat.androidbase.utils.TextUtilities
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun Long?.toDateMonthName(): String {
    return format("dd MMM", this)
}

fun Long?.toDateMonthYearTime(): String {
    return format("dd/MM/yyyy hh:MM a", this)
}

fun Long?.toDateMonthYear(): String {
    return format("dd-MMM-yyyy", this)
}

fun format(dateFormat: String, timeInSec: Long?) = try {
    timeInSec?.let {
        val sdf = SimpleDateFormat(dateFormat, Locale(LedgerSDK.locale, "in"))
        sdf.format(it * 1000)
    } ?: ""
} catch (ex: Exception) {
    ex.printStackTrace()
    timeInSec.toString()
}

fun formatDecimal(value: Double?, fractionDigitCount: Int = 2): String {
    val formatter = NumberFormat.getNumberInstance(Locale(LedgerSDK.locale, "in"))
    formatter.minimumFractionDigits = fractionDigitCount
    formatter.maximumFractionDigits = fractionDigitCount
    return formatter.format(value ?: 0.0)
}

val sdf = SimpleDateFormat("dd-MMM-yyyy", NumberUtilities.locale)

object Utils {
    fun openDatePickerDialog(
        context: Context,
        dateFormat: SimpleDateFormat = sdf,
        selectedDate: (String) -> Unit
    ) {
        val cal = Calendar.getInstance()
        val currentDate = dateFormat.format(cal.time)
        if (!TextUtilities.isNullCase(currentDate)) {
            var date: Date? = null
            try {
                date = dateFormat.parse(currentDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (date != null) cal.time = date
        }
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val calendar = Calendar.getInstance()
                calendar[year, monthOfYear] = dayOfMonth
                selectedDate(dateFormat.format(calendar.time))
            }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun openDatePicker(
        context: Context,
        dateFormat: SimpleDateFormat = sdf,
        selectedDate: (String, Long) -> Unit
    ) {
        val cal = Calendar.getInstance()
        val currentDate = dateFormat.format(cal.time)
        if (!TextUtilities.isNullCase(currentDate)) {
            var date: Date? = null
            try {
                date = dateFormat.parse(currentDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (date != null) cal.time = date
        }
        val datePickerDialog = DatePickerDialog(
            context,
            { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val calendar = Calendar.getInstance()
                calendar[year, monthOfYear] = dayOfMonth
                selectedDate(dateFormat.format(calendar.time), calendar.time.time)
            }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}
