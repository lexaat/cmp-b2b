package features.auth.domain

import features.auth.model.AuthResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResponse
    suspend fun verifyOtp(username: String, password: String, otp: String): AuthResponse
    suspend fun changePassword(newPassword: String, otp: String): AuthResponse
}
