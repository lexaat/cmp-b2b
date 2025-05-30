package features.auth.domain

import data.model.ApiResponse
import features.auth.domain.model.AuthResult
import features.auth.domain.model.LoginRequest
import features.auth.domain.model.RefreshTokenRequest

interface AuthRepository {
    suspend fun login(request: LoginRequest): ApiResponse<AuthResult>
    suspend fun verifyOtp(username: String, password: String, otp: String): ApiResponse<AuthResult>
    suspend fun changePassword(username: String, password: String, newPassword: String, otp: String): ApiResponse<AuthResult>
    suspend fun refreshToken(request: RefreshTokenRequest): ApiResponse<AuthResult>
}
