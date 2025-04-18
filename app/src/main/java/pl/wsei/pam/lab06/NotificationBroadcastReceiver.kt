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

        val taskTitle = intent?.getStringExtra(titleExtra) ?: "Nieznane zadanie"
        val message = intent?.getStringExtra(messageExtra) ?: "Brak treści"
        val isTomorrow = intent?.getBooleanExtra("deadlineTomorrow", false) ?: false

        Log.d("NotificationReceiver", "Alarm otrzymany dla zadania: $taskTitle, deadlineTomorrow: $isTomorrow")


        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(if (isTomorrow) "Deadline jutro!" else "Zbliża się deadline")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}
