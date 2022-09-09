package lib.dehaat.ledger.presentation.model.transactions

sealed class DaysToFilter {
    object All : DaysToFilter()
    object SevenDays : DaysToFilter()
    object OneMonth : DaysToFilter()
    object ThreeMonth : DaysToFilter()
    data class CustomDays(val fromDateMilliSec: Long, val toDateMilliSec: Long) : DaysToFilter()
}

fun DaysToFilter.toStartAndEndDates(): Pair<Long, Long>? = when (this) {
    DaysToFilter.All -> null
    DaysToFilter.SevenDays -> {
        val time = calculateTimeInMillisecond(7)
        Pair(time.first, time.second)
    }
    DaysToFilter.OneMonth -> {
        val time = calculateTimeInMillisecond(31)
        Pair(time.first, time.second)

    }
    DaysToFilter.ThreeMonth -> {
        val time = calculateTimeInMillisecond(93)
        Pair(time.first, time.second)
    }
    is DaysToFilter.CustomDays -> {
        val currentDaySec = toDateMilliSec / 1000
        val pastDaySec = fromDateMilliSec / 1000
        Pair(pastDaySec, currentDaySec)
    }
}

private fun calculateTimeInMillisecond(dayCount: Int): Pair<Long, Long> {
    val daysSec = dayCount * 24 * 60 * 60
    val currentDaySec = System.currentTimeMillis() / 1000
    val pastDaySec = currentDaySec.minus(daysSec)
    return Pair(pastDaySec, currentDaySec)
}
