package data.model.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val aid: Long,
    val account: String,
    val name: String,
    val branch: String,
    val state: Int,
    val sgn: String,
    @SerialName("val")val valuta: String,
    val dt: Long,
    val ct: Long,
    val o_date: String,
    val l_date: String,
    val s_in: Long,
    val s_out: Long,
    val canpay: Int,
    val notif_in: Int,
    val notif_out: Int,
)