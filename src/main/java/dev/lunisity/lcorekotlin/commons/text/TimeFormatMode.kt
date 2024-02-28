package dev.lunisity.lcorekotlin.commons.text

import java.util.concurrent.TimeUnit

enum class TimeFormatMode(
    val months: String,
    val days: String,
    val hours: String,
    val minutes: String,
    val seconds: String
) {
    FULL(" months", " days", " hours", " minutes", " seconds"),
    SHORT(" mo", " d", " h", " m", " s");

    fun apply(time: Long, timeUnit: TimeUnit): String {
        if (time <= 0) {
            return "now"
        }

        val textJoiner = StringBuilder(", ")
        val milliseconds = timeUnit.toMillis(time)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % 60
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60
        val hours = TimeUnit.MILLISECONDS.toHours(milliseconds) % 24
        val days = TimeUnit.MILLISECONDS.toDays(milliseconds)
        val months = days / 30

        if (months > 0) {
            textJoiner.append(months).append(this.months)
        }

        if (days > 0) {
            textJoiner.append(days).append(this.days)
        }
        if (hours > 0) {
            textJoiner.append(hours).append(this.hours)
        }
        if (minutes > 0) {
            textJoiner.append(minutes).append(this.minutes)
        }
        if (seconds > 0) {
            textJoiner.append(seconds).append(this.seconds)
        }

        if (days == 0L && hours == 0L && minutes == 0L && seconds == 0L) {
            textJoiner.append("now")
        }
        return textJoiner.toString()
    }
}