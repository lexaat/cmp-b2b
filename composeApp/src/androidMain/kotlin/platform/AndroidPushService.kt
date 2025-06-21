package platform

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import android.util.Log

class AndroidPushService : PushService {

    override fun initialize() {
        Firebase.messaging.isAutoInitEnabled = true
    }

    override fun getToken(onToken: (String?) -> Unit) {
        Firebase.messaging.token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onToken(task.result)
            } else {
                Log.e("PushService", "Failed to get FCM token", task.exception)
                onToken(null)
            }
        }
    }
}