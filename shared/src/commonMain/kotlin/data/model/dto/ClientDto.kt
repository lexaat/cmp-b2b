package data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClientDto(
    val id: Long,
    val name: String,
    val code: String,
    val inn: String,
    val address: String,
    val phone: String,
    val branch: String,
    val doc_num: String,
    val buh_name: String,
    val dir_name: String,
    val oper_day: String,
    val payment_scheme: String,
    val cards_count: Int,
    val cert_sn: String,
    val pinfl: String?,
    val k2_count: Int,
    val accounts: List<AccountDto>,
    val zp_upi_accs: List<ZpAccountDto>?
)




