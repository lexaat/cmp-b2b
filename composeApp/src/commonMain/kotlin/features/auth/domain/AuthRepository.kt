package features.auth.domain

import features.auth.model.AuthResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResponse
    suspend fun verifyOtp(username: String, password: String, otp: String): AuthResponse
    suspend fun changePassword(username: String, password: String, newPassword: String, otp: String): AuthResponse
    suspend fun refreshToken(refreshToken: String): AuthResponse
}
