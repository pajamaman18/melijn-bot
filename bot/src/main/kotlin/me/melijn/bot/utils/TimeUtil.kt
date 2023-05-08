package me.melijn.bot.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinLocalDateTime
import net.dv8tion.jda.api.entities.ISnowflake
import net.dv8tion.jda.api.utils.TimeFormat
import kotlin.time.Duration
import kotlin.time.toKotlinDuration

object TimeUtil {
    fun java.time.Duration.formatElapsed(): String = this.toKotlinDuration().formatElapsed()
    fun Duration.formatElapsed(): String {
        val millis = inWholeMilliseconds % 1000
        val seconds = inWholeSeconds % 60
        val minutes = inWholeMinutes % 60
        val hours = inWholeHours % 24
        val days = inWholeDays % 365

        return when {
            days == 0L && hours == 0L && minutes == 0L && seconds < 3 -> String.format("0:%02d.%03d", seconds, millis)
            days == 0L && hours == 0L -> String.format("%d:%02d", minutes, seconds)
            days == 0L -> String.format("%d:%02d:%02d", hours, minutes, seconds)
            else -> String.format("%d days %d:%02d:%02d", days, hours, minutes, seconds)
        }
    }

    /**
     * `1 second 30 milliseconds` (<3 seconds)
     *
     * `1 minute 30 seconds`, `1 hour 1 minute 30 seconds`, `1 day, 2 hours 3 minutes 30 seconds`
     */
    fun Duration.formatElapsedVerbose(): String {
        val millis = inWholeMilliseconds % 1000
        val seconds = inWholeSeconds % 60
        val minutes = inWholeMinutes % 60
        val hours = inWholeHours % 24
        val days = inWholeDays % 365

        fun Long.s() = if (this != 1L) "s" else ""

        return when {
            days == 0L && hours == 0L && minutes == 0L && seconds < 3 ->
                String.format("%d second%s %d millisecond%s", seconds, seconds.s(), millis, millis.s())
            days == 0L && hours == 0L ->
                String.format("%d minute%s %d second%s", minutes, minutes.s(), seconds, seconds.s())
            days == 0L ->
                String.format("%d hour%s %d minute%s %d second%s", hours, hours.s(), minutes, minutes.s(), seconds, seconds.s())
            else ->
                String.format("%d day%s %d hour%s %d minute%s %d second%s", days, days.s(), hours, hours.s(), minutes, minutes.s(), seconds, seconds.s())
        }
    }

    fun java.time.Duration.formatRelative(): String = this.toKotlinDuration().formatRelative()

    fun Duration.formatRelative(): String {
        val lastPoint = System.currentTimeMillis() - inWholeMilliseconds
        return TimeFormat.RELATIVE.format(Instant.fromEpochMilliseconds(lastPoint))
    }

    fun now(): LocalDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime()
    fun between(time1: Instant, time2: Instant): Duration {
        return java.time.Duration.between(time1.toJavaInstant(), time2.toJavaInstant()).toKotlinDuration()
    }

    fun TimeFormat.format(createdAt: Instant): String = format(createdAt.toJavaInstant())
    fun TimeFormat.format(flake: ISnowflake): String = format(flake.timeCreated)
}