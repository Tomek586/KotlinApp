package pl.wsei.pam.lab06.data

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


fun calculateAlarmTime(deadlineDate: LocalDate, defaultHour: Int = 8, defaultMinute: Int = 0): Long {
    val defaultTime = LocalTime.of(defaultHour, defaultMinute) // np. 08:00
    val deadlineDateTime = LocalDateTime.of(deadlineDate, defaultTime)
    return deadlineDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}