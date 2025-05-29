package di

import app.AppViewModel
import app.ThemeViewModel
import cafe.adriel.voyager.navigator.Navigator
import config.AppConfig
import config.Config
import core.error.ApiCallHandler
import core.error.GlobalErrorHandler
import data.theme.ThemeRepository
import data.theme.ThemeRepositoryImpl
import database.AppDatabase
import features.auth.data.AuthRepositoryImpl
import features.auth.domain.AuthRepository
import features.auth.presentation.login.AuthViewModel
import features.auth.presentation.otp.OtpViewModel
import features.common.data.auth.PersistentTokenManager
import features.common.domain.auth.TokenManager
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock
import networking.HttpClientFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<TokenManager> { PersistentTokenManager(get()) }
    single<ThemeRepository> { ThemeRepositoryImpl(get()) }

    single<HttpClient> {
        HttpClientFactory(get()).create()
    }

    single<AppConfig> { Config.current }
    single<Clock> { Clock.System }

    single { AppDatabase(get()) } // Инициализируй свою базу данных тут
    single { get<AppDatabase>().homeQueries }

    viewModel { ThemeViewModel(get()) }
    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get(),
            config = get()
        )
    }

    factory { (navigator: Navigator) ->
        GlobalErrorHandler(navigator, get())
    }

    single { ApiCallHandler(get(), get(), get()) }

    single { AppViewModel(get(), get(), get()) }
    single { AuthViewModel(get(), get(), get(), get()) }
    single { OtpViewModel(get(), get()) }
}