package pl.wsei.pam.lab06

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import pl.wsei.pam.lab06.data.TodoApplication
import pl.wsei.pam.lab06.ui.theme.Lab06Theme

// Stałe używane w powiadomieniach i alarmach
const val notificationID = 121
const val channelID = "Lab06 channel"
const val titleExtra = "title"
const val messageExtra = "message"

class MainActivity : ComponentActivity() {

    companion object {
        // Udostępniany kontener DI
        lateinit var container: pl.wsei.pam.lab06.data.AppContainer
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Lab06 channel"
            val descriptionText = "Lab06 is channel for notifications for approaching tasks."
            // Możesz zmienić IMPORTANCE, jeśli chcesz heads-up powiadomienia
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Nowa metoda scheduleAlarm przyjmująca dodatkowy parametr taskTitle
    fun scheduleAlarm(time: Long, taskTitle: String) {
        val intent = Intent(applicationContext, NotificationBroadcastReceiver::class.java).apply {
            // Wysyłamy jako extras tytuł zadania oraz komunikat
            putExtra(titleExtra, "Termin zadania: $taskTitle")
            putExtra(messageExtra, "Zbliża się termin wykonania zadania: $taskTitle")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        Log.d("MainActivity", "Alarm ustawiony na: $time dla zadania: $taskTitle")
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        // Inicjalizacja kontenera DI
        container = (this.application as TodoApplication).container

        // Testowy alarm: po 2 sekundach od uruchomienia ustawiamy alarm dla przykładowego zadania "Test Task"
        scheduleAlarm(System.currentTimeMillis() + 2000, "Test Task")

        setContent {
            Lab06Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}
