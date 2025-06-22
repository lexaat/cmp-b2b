package uz.hb.b2b.notifications_core.push

import platform.Foundation.NSLog

class iOSPushService : PushService {
    override fun initialize() {
        println("✅ iOS PushService initialized")
        // Здесь инициализация iOS пушей (Firebase + delegate и т.д.)
    }

    override fun handleMessage(payload: PushPayload) {
        PushNavigator.navigate(payload.type)
    }
}