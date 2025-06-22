package uz.hb.b2b.notifications_core

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

actual class PushServiceImpl actual constructor(
    private val navigator: PushNavigator
) : PushService {
    override fun initialize() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("PushService", "📲 FCM token: $token")
            }
        }
    }

    override fun handleRemoteMessage(payload: Map<String, Any?>) {
        val aps = payload["aps"] as? Map<*, *>
        val alert = aps?.get("alert") as? Map<*, *>
        val title = alert?.get("title") as? String
        val body = alert?.get("body") as? String
        val type = PushType.from(payload["type"] as? String)

        val push = PushPayload(
            title = title,
            body = body,
            type = type,
            data = payload
        )

        Log.d("PushService", "📥 Пуш получен: $push")
        navigator.navigateTo(push)
    }
}