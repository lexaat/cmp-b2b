package data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class ZpAccountDto(
    val account: String,
    val branch: String
)