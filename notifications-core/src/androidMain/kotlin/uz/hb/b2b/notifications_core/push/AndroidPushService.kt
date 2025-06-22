package uz.hb.b2b.notifications_core.push

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class AndroidPushService : PushService {
    override fun initialize() {
        println("✅ Android PushService initialized")
        // Здесь должна быть логика подписки на пуши (FCM init)
    }

    override fun handleMessage(payload: PushPayload) {
        PushNavigator.navigate(payload.type)
    }
}