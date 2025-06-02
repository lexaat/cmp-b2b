package features.auth.domain.usecase

import data.model.ApiResponse
import features.auth.domain.AuthRepository
import features.auth.domain.model.AuthResult
import features.auth.domain.model.ChangePasswordRequest

class ChangePasswordUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: ChangePasswordRequest): ApiResponse<AuthResult> {
        return repository.changePassword(request)
    }
}