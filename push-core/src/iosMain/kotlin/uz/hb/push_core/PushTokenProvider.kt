package push

import platform.Foundation.NSUserDefaults

class IOSPushTokenProvider : PushTokenProvider {
    override suspend fun getPushToken(): String? {
        val token = NSUserDefaults.standardUserDefaults.stringForKey("fcm_token")
        if (token != null) {
            println("📱 iOS FCM token received: $token")
        } else {
            println("📱 iOS FCM token is not available")
        }
        return token
    }
}