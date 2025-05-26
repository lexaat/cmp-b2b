package platform

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import platform.Foundation.NSError
import kotlin.coroutines.resume

class IOSBiometricAuthenticator : BiometricAuthenticator {
    override suspend fun authenticate(reason: String): BiometricResult =
        suspendCancellableCoroutine { cont ->
            val context = LAContext()
            context.evaluatePolicy(
                policy = LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                localizedReason = reason,
                reply = { success, error ->
                    if (success) {
                        cont.resume(BiometricResult.Success)
                    } else {
                        val errorMsg = (error as NSError?)?.localizedDescription ?: "Unknown error"
                        cont.resume(BiometricResult.Failed(errorMsg))
                    }
                }
            )
        }
}
