package pl.wsei.pam.lab06.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Klasa odpowiedzialna za zarządzanie zapisanymi ustawieniami powiadomień.
 * Preferencje są zapisane w pliku o nazwie PREFS_NAME.
 */
class SettingsPreferenceManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "notification_settings"
        private const val KEY_DAYS_BEFORE = "days_before"
        private const val KEY_HOURS_BEFORE = "hours_before"
        private const val KEY_REPEAT_COUNT = "repeat_count"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getNotificationConfig(): NotificationConfig {
        val days = sharedPreferences.getInt(KEY_DAYS_BEFORE, 1)
        val hours = sharedPreferences.getInt(KEY_HOURS_BEFORE, 0)
        val repeat = sharedPreferences.getInt(KEY_REPEAT_COUNT, 1)
        return NotificationConfig(daysBefore = days, hoursBefore = hours, repeatCount = repeat)
    }

    fun saveNotificationConfig(config: NotificationConfig) {
        with(sharedPreferences.edit()) {
            putInt(KEY_DAYS_BEFORE, config.daysBefore)
            putInt(KEY_HOURS_BEFORE, config.hoursBefore)
            putInt(KEY_REPEAT_COUNT, config.repeatCount)
            apply()
        }
    }
}
