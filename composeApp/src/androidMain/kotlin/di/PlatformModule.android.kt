package di

import android.app.Application
import androidx.fragment.app.FragmentActivity
import org.koin.android.ext.koin.androidContext
import platform.AndroidBiometricAuthenticator
import platform.BiometricAuthenticator

lateinit var biometricActivity: FragmentActivity

actual fun provideBiometricAuthenticator(): BiometricAuthenticator {
    return AndroidBiometricAuthenticator(biometricActivity)
}