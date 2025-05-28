package platform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics
import platform.darwin.NSObject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BiometricAuthenticatorIos : BiometricAuthenticator {
    @OptIn(ExperimentalForeignApi::class)
    override suspend fun authenticate(reason: String): BiometricResult =
        suspendCancellableCoroutine { cont ->
            val context = LAContext()
            val canEvaluate = context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)

            if (canEvaluate) {
                context.evaluatePolicy(
                    LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                    reason
                ) { success, error ->
                    if (success) {
                        cont.resume(BiometricResult.Success)
                    } else {
                        cont.resume(BiometricResult.Failed(error?.localizedDescription ?: "Ошибка"))
                    }
                }
            } else {
                cont.resume(BiometricResult.Failed("Биометрия недоступна"))
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun isBiometricAvailable(): Boolean {
        val context = LAContext()
        return context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)
    }
}
