package uz.hb.b2b.notifications_core

import platform.Foundation.NSDictionary
import platform.Foundation.NSLog

actual class PushServiceImpl actual constructor(
    private val navigator: PushNavigator
) : PushService {
    override fun initialize() {
        NSLog("📲 Push initialized on iOS")
    }

    override fun handleRemoteMessage(payload: Map<String, Any?>) {
        val aps = payload["aps"] as? Map<*, *>
        val alert = aps?.get("alert") as? Map<*, *>
        val title = alert?.get("title") as? String
        val body = alert?.get("body") as? String
        val type = PushType.from(payload["type"] as? String)

        val push = PushPayload(
            title = title,
            body = body,
            type = type,
            data = payload
        )

        NSLog("📥 Пуш получен на iOS: \n$push")
        navigator.navigateTo(push)
    }
}