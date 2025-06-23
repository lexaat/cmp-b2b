package di

import app.AppViewModel
import app.ThemeViewModel
import cafe.adriel.voyager.navigator.Navigator
import config.AppConfig
import config.Config
import core.error.GlobalErrorHandler
import data.local.adapter.accountEntityAdapter
import data.local.adapter.clientEntityAdapter
import data.theme.ThemeRepository
import data.theme.ThemeRepositoryImpl
import b2b.database.AppDatabase
import features.auth.data.AuthRepositoryImpl
import features.auth.domain.AuthRepository
import features.common.data.auth.PersistentTokenManager
import features.common.domain.auth.TokenManager
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock
import networking.HttpClientFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import core.usecase.RefreshWrapper
import push.pushModule

val appModule = module {

    single<TokenManager> { PersistentTokenManager(get()) }
    single<ThemeRepository> { ThemeRepositoryImpl(get()) }

    single<HttpClient> {
        HttpClientFactory(get()).create()
    }

    includes(pushModule())

    single<AppConfig> { Config.current }
    single<Clock> { Clock.System }

    single { AppDatabase(
        driver = get(),
        ClientEntityAdapter = clientEntityAdapter,
        AccountEntityAdapter = accountEntityAdapter) }
    single { get<AppDatabase>().clientQueries }
    single { get<AppDatabase>().accountQueries }
    single { get<AppDatabase>().dataSyncMetaQueries }

    viewModel { ThemeViewModel(get()) }
    single<AuthRepository> {
        AuthRepositoryImpl(
            httpClient = get(),
            config = get()
        )
    }

    single { RefreshWrapper(get(), get(), get()) }

    factory { (navigator: Navigator) ->
        GlobalErrorHandler(navigator, get())
    }

    single { AppViewModel(get(), get(), get()) }
}