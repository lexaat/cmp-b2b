package platform

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CompletableDeferred

class AndroidBiometricAuthenticator(private val context: Context) : BiometricAuthenticator {

    override suspend fun authenticate(reason: String): BiometricResult {
        val result = CompletableDeferred<BiometricResult>()
        val executor = ContextCompat.getMainExecutor(context)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(reason)
            .setNegativeButtonText("Cancel")
            .build()

        val biometricPrompt = BiometricPrompt(
            context as androidx.fragment.app.FragmentActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(resultAuth: BiometricPrompt.AuthenticationResult) {
                    result.complete(BiometricResult.Success)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    result.complete(BiometricResult.Failed(errString.toString()))
                }

                override fun onAuthenticationFailed() {
                    result.complete(BiometricResult.Failed("Authentication failed"))
                }
            })

        biometricPrompt.authenticate(promptInfo)

        return result.await()
    }
}
