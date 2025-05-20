package features.home.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientDto(
    val name: String,
    val inn: String,
    val address: String,
    val accounts: List<AccountDto>
)

@Serializable
data class AccountDto(
    val account: String,
    val s_in: Long,
    val s_out: Long
)