package uz.hb.b2b.notifications_core

enum class PushType(val raw: String) {
    PAYMENT("payment"),
    MESSAGE("message"),
    UNKNOWN("unknown");

    companion object {
        fun from(raw: String?): PushType = entries.firstOrNull { it.raw == raw } ?: UNKNOWN
    }
}