package features.auth.domain

import data.model.ApiResponse
import features.auth.model.AuthResult

interface AuthRepository {
    suspend fun login(username: String, password: String): ApiResponse<AuthResult>
    suspend fun verifyOtp(username: String, password: String, otp: String): ApiResponse<AuthResult>
    suspend fun changePassword(username: String, password: String, newPassword: String, otp: String): ApiResponse<AuthResult>
    suspend fun refreshToken(refreshToken: String): ApiResponse<AuthResult>
}
