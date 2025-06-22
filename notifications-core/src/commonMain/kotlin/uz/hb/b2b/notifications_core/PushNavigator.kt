package uz.hb.b2b.notifications_core

interface PushNavigator {
    fun navigateTo(push: PushPayload)
}