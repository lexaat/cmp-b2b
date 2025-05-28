package di

import app.AppViewModel
import app.ThemeViewModel
import config.AppConfig
import config.Config
import data.theme.ThemeRepository
import data.theme.ThemeRepositoryImpl
import features.auth.data.AuthRepositoryImpl
import features.auth.domain.AuthRepository
import features.auth.presentation.login.LoginViewModel
import features.auth.presentation.login_otp.OtpViewModel
import features.common.data.auth.PersistentTokenManager
import features.common.domain.auth.TokenManager
import io.ktor.client.HttpClient
import networking.HttpClientFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import platform.BiometricAuthenticator

val appModule = module {

    single<TokenManager> { PersistentTokenManager(get()) }
    single<ThemeRepository> { ThemeRepositoryImpl(get()) }

    single<HttpClient> {
        HttpClientFactory(get()).create()
    }

    single<AppConfig> { Config.current }

    viewModel { ThemeViewModel(get()) }
    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get(),
            config = get()
        )
    }

    single { AppViewModel(get(), get(), get()) }
    single { LoginViewModel(get(), get(), get()) }
    single { OtpViewModel(get(), get()) }
}