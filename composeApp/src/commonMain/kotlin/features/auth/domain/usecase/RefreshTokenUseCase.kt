package features.auth.domain.usecase

import data.model.ApiResponse
import features.auth.domain.AuthRepository
import features.auth.domain.model.AuthResult
import features.auth.domain.model.LoginRequest
import features.auth.domain.model.RefreshTokenRequest

class RefreshTokenUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: RefreshTokenRequest): ApiResponse<AuthResult> {
        return repository.refreshToken(request)
    }
}