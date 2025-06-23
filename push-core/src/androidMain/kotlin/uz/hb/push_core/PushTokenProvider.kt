package push

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import uz.hb.push_core.PushTokenProvider

class AndroidPushTokenProvider(
    private val context: Context
) : PushTokenProvider {
    override suspend fun getPushToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            null
        }
    }
}