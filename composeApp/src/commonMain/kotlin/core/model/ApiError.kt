package core.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val code: Int,
    val message: String
)