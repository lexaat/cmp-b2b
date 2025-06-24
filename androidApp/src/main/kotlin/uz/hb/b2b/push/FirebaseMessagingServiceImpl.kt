package uz.hb.b2b.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingServiceImpl : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Push", "New token: $token")
        // TODO: Отправить токен на сервер при необходимости
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Push", "Push пришёл: ${message.data}")

        // Если пуш содержит `notification`-часть (заголовок и текст)
        val notification = message.notification
        if (notification != null) {
            val title = notification.title ?: "Hayot B2B"
            val body = notification.body ?: "Новое уведомление"
            NotificationHelper.showNotification(this, title, body)
            return
        }

        // Если пуш содержит только `data`-часть
        val title = message.data["title"] ?: "Hayot B2B"
        val body = message.data["body"] ?: "Новое уведомление"
        NotificationHelper.showNotification(this, title, body)
    }
}