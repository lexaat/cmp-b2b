package push

import platform.Foundation.NSUserDefaults

class IOSPushTokenProvider : PushTokenProvider {
    override suspend fun getPushToken(): String? {
        // Реализация через SharedPreferences, UserDefaults, или callback из AppDelegate
        // Здесь просто пример, как бы это выглядело:
        // На практике пуш-токен ты получаешь в AppDelegate и сохраняешь куда-то (например, в UserDefaults),
        // а тут просто забираешь.
        return NSUserDefaults.standardUserDefaults.stringForKey("fcm_token")
    }
}