package platform

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.core.app.NotificationCompat

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("Push", "📩 Push received: ${remoteMessage.notification?.body}")

        val title = remoteMessage.notification?.title ?: "🔥 Title"
        val body = remoteMessage.notification?.body ?: "📩 Body"

        Log.d("PushHandler", "🚀 Показываем уведомление: $title")
        showNotification(title, body)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    override fun onNewToken(token: String) {
        Log.d("Push", "🔄 New token: $token")
        // Можно отправить токен на сервер
    }
}