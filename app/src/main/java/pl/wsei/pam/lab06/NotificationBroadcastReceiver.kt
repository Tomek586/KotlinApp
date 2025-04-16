package pl.wsei.pam.lab06

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.util.Log
import pl.wsei.pam.lab01.R

class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Pobieramy tytuł i treść powiadomienia z intencji
        val taskTitle = intent?.getStringExtra(titleExtra) ?: "Brak tytułu"
        val message = intent?.getStringExtra(messageExtra) ?: "Brak treści"
        Log.d("NotificationReceiver", "Alarm otrzymany dla zadania: $taskTitle")
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(taskTitle)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}
