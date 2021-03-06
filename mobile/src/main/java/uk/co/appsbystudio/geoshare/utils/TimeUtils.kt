package uk.co.appsbystudio.geoshare.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS

fun Long.convertDate(): String {
    var dateMilli = this
    if (this < 1000000000000L) {
        //Timestamp is in seconds
        dateMilli *= 1000
    }

    val currentTime = System.currentTimeMillis()
    if (dateMilli > currentTime || dateMilli <= 0) {
        return ""
    }

    val date = Date(dateMilli)
    val currentDate = Date(currentTime)

    val lessThanSevenDays = SimpleDateFormat("EEEE", Locale.getDefault())
    val moreThanSevenDays = SimpleDateFormat("MMM dd", Locale.getDefault())

    return getString(dateMilli, currentTime, date, currentDate, lessThanSevenDays, moreThanSevenDays)
}

private fun getString(dateMilli: Long?, currentTime: Long, date: Date, currentDate: Date, lessThanSevenDays: SimpleDateFormat, moreThanSevenDays: SimpleDateFormat): String {
    val diff = currentTime - dateMilli!!
    when {
        diff < MINUTE_MILLIS -> return "Just now"
        diff < 2 * MINUTE_MILLIS -> return "a minute ago"
        diff < 50 * MINUTE_MILLIS -> return (diff / MINUTE_MILLIS).toString() + " mins ago"
        diff < 90 * MINUTE_MILLIS -> return "an hour ago"
        diff < 24 * HOUR_MILLIS -> return (diff / HOUR_MILLIS).toString() + " hours ago"
        diff < 48 * HOUR_MILLIS -> return "Yesterday"
        else -> {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, 7)

            val formattedDate: String
            formattedDate = if (calendar.time < currentDate) {
                moreThanSevenDays.format(date)
            } else {
                lessThanSevenDays.format(date)
            }

            return formattedDate
        }
    }
}
