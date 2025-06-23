package push

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await


class AndroidPushTokenProvider(
    private val context: Context
) : PushTokenProvider {
    override suspend fun getPushToken(): String? {
        return try {
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d("PushTokenProvider", "Android FCM token received: $token")
            token
        } catch (e: Exception) {
            Log.e("PushTokenProvider", "Error getting Android FCM token", e)
            null
        }
    }
}