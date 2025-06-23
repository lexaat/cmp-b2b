package domain.model

data class Account(
    val aid: Long,
    val account: String,
    val name: String,
    val branch: String,
    val state: Int,
    val sgn: String,
    val valuta: String,
    val dt: Long,
    val ct: Long,
    val oDate: String,
    val lDate: String,
    val sIn: Long,
    val sOut: Long,
    val canPay: Int,
    val notifIn: Int,
    val notifOut: Int
)