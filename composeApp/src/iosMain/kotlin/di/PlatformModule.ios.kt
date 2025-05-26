package di

import platform.BiometricAuthenticator
import platform.IOSBiometricAuthenticator

actual fun provideBiometricAuthenticator(): BiometricAuthenticator {
    return IOSBiometricAuthenticator()
}