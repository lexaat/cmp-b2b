package di

import org.koin.dsl.module
import platform.BiometricAuthenticator
import platform.BiometricAuthenticatorIos

fun bioModule() = module {
    single<BiometricAuthenticator> { BiometricAuthenticatorIos() }
}