package uz.hb.b2b.notifications_core.push

data class PushPayload(
    val title: String?,
    val body: String?,
    val type: PushType,
    val data: Map<String, String>
)