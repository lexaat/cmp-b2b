package features.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    @SerialName("new_password") val newPassword: String,
    val otp: String
)