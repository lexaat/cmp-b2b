package uz.hb.b2b.notifications_core

expect class PushServiceImpl(
    navigator: PushNavigator
) : PushService

interface PushService {
    fun initialize()
    fun handleRemoteMessage(payload: Map<String, Any?>)
}