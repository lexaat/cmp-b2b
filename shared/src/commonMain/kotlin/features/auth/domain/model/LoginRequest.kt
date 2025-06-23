package features.auth.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class LoginRequest(
    @Transient val username: String = "",
    @Transient val password: String = "",
    val otp: String
)