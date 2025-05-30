package features.auth.domain.usecase

import data.model.ApiResponse
import features.auth.domain.AuthRepository
import features.auth.domain.model.AuthResult
import features.auth.domain.model.LoginRequest

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: LoginRequest): ApiResponse<AuthResult> {
        return repository.login(request)
    }
}