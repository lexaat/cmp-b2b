package domain.model

data class Client(
    val id: Long,
    val name: String,
    val code: String,
    val inn: String,
    val address: String,
    val phone: String,
    val branch: String,
    val docNum: String,
    val buhName: String,
    val dirName: String,
    val operDay: String,
    val paymentScheme: String,
    val cardsCount: Int,
    val certSn: String,
    val pinfl: String?,
    val k2Count: Int,
    val accounts: List<Account>
)