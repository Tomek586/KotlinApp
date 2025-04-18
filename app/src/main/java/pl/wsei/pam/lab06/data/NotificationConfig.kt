package pl.wsei.pam.lab06.data

data class NotificationConfig(
    val daysBefore: Int = 1,
    val hoursBefore: Int = 0,
    val repeatCount: Int = 1
)
