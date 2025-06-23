package features.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ChangePasswordRequest(
    @Transient val username: String = "",
    @Transient val password: String = "",
    @SerialName("new_password") val newPassword: String,
    val otp: String
)