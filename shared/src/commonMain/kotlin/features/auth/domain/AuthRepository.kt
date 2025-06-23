package features.auth.domain

import data.model.ApiResponse
import features.auth.domain.model.AuthResult
import features.auth.domain.model.ChangePasswordRequest
import features.auth.domain.model.LoginRequest
import features.auth.domain.model.RefreshTokenRequest

interface AuthRepository {
    suspend fun login(request: LoginRequest): ApiResponse<AuthResult>
    suspend fun verifyOtp(request: LoginRequest): ApiResponse<AuthResult>
    suspend fun changePassword(request: ChangePasswordRequest): ApiResponse<String>
    suspend fun refreshToken(request: RefreshTokenRequest): ApiResponse<AuthResult>
}
