package features.auth.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refresh_token: String)