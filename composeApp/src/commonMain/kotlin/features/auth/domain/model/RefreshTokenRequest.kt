package features.auth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refresh_token: String)