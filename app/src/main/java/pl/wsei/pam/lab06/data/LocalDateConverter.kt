package pl.wsei.pam.lab06.data

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateConverter {
    companion object {
        const val pattern = "yyyy-MM-dd"

        fun toMillis(date: LocalDate): Long {
            return date.toEpochDay() * 24 * 60 * 60 * 1000
        }

        fun fromMillis(millis: Long): LocalDate {
            return LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
        }
    }

    @TypeConverter
    fun fromDateTime(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofPattern(pattern))
    }

    @TypeConverter
    fun fromDateTime(str: String): LocalDate {
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern))
    }
}
