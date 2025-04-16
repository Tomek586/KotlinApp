package pl.wsei.pam.lab06.data

/**
 * Model konfiguracji powiadomień, który przechowuje ustawienia:
 * - ile dni przed terminem wysłać powiadomienie,
 * - ile godzin przed terminem,
 * - ile razy powtarzać powiadomienia.
 */
data class NotificationConfig(
    val daysBefore: Int = 1,
    val hoursBefore: Int = 0,
    val repeatCount: Int = 1
)
