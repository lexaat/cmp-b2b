package di

import androidx.fragment.app.FragmentActivity
import platform.AndroidBiometricAuthenticator
import platform.BiometricAuthenticator

lateinit var biometricActivity: FragmentActivity

actual fun provideBiometricAuthenticator(): BiometricAuthenticator {
    return AndroidBiometricAuthenticator(biometricActivity)
}