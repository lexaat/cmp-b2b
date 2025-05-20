package di

import config.AppConfig
import config.Config
import features.auth.domain.AuthRepository
import features.auth.domain.AuthRepositoryImpl
import features.auth.presentation.AuthViewModel
import features.common.data.auth.PersistentTokenManager
import features.common.domain.auth.TokenManager
import networking.HttpClientFactory
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import org.koin.dsl.module

val appModule = module {
    single<TokenManager> { PersistentTokenManager(Settings()) }

    single<HttpClient> {
        HttpClientFactory(get()).create()
    }

    single<AppConfig> { Config.current }

    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get(),
            config = get()
        )
    }

    single { AuthViewModel(get(), get()) }
}