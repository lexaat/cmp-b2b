package features.home.domain.model

data class Client(
    val name: String,
    val inn: String,
    val address: String,
    val accounts: List<Account>
)

data class Account(
    val account: String,
    val balanceIn: Long,
    val balanceOut: Long
)
