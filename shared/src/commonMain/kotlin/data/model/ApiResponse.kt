package data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val id: Int? = null,
    val error: ApiError? = null,
    val result: T? = null
)
