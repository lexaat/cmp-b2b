package features.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val otp: String)