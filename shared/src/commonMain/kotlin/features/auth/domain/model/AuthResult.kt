package features.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResult(
    @SerialName("access_token") val accessToken: String,
    @SerialName("access_token_expires_in") val accessTokenExpiresIn: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("refresh_token_expires_in") val refreshTokenExpiresIn: String,
    @SerialName("token_type") val tokenType: String
)


