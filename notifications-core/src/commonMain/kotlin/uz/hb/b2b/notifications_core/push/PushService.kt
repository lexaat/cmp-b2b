package uz.hb.b2b.notifications_core.push

interface PushService {
    fun initialize()
    fun handleMessage(payload: PushPayload)
}

