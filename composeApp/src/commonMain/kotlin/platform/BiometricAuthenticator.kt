package platform

interface BiometricAuthenticator {
    suspend fun authenticate(reason: String): BiometricResult
}

sealed class BiometricResult {
    object Success : BiometricResult()
    data class Failed(val message: String) : BiometricResult()
}