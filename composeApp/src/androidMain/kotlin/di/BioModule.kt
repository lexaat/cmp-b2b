package di

import androidx.fragment.app.FragmentActivity
import org.koin.dsl.module
import platform.BiometricAuthenticator
import platform.BiometricAuthenticatorAndroid

fun bioModule(activity: FragmentActivity) = module {
    single<BiometricAuthenticator> { BiometricAuthenticatorAndroid(activity) }
}