package di

import app.AppViewModel
import app.ThemeViewModel
import config.AppConfig
import config.Config
import data.theme.ThemeRepository
import data.theme.ThemeRepositoryImpl
import features.auth.domain.AuthRepository
import features.auth.data.AuthRepositoryImpl
import features.auth.presentation.login.LoginViewModel
import features.common.data.auth.PersistentTokenManager
import features.common.domain.auth.TokenManager
import networking.HttpClientFactory
import com.russhwolf.settings.Settings
import features.auth.presentation.login_otp.OtpViewModel
import io.ktor.client.HttpClient
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val appModule = module {

    single<TokenManager> { PersistentTokenManager(Settings()) }

    single<HttpClient> {
        HttpClientFactory(get()).create()
    }

    single<AppConfig> { Config.current }


    single<ThemeRepository> { ThemeRepositoryImpl(Settings()) }
    viewModel { ThemeViewModel(get()) }

    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get(),
            config = get()
        )
    }

    single { AppViewModel(get(), get()) }
    single { LoginViewModel(get(), get()) }
    single { OtpViewModel(get(), get()) }
}