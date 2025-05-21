package model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val id: Int? = null,
    val error: ApiError? = null,
    val result: T? = null
)

@Serializable
data class ApiError(
    val code: Int,
    val message: String?
)