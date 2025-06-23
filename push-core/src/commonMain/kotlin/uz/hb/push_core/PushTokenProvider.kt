package push

interface PushTokenProvider {
    suspend fun getPushToken(): String?
}