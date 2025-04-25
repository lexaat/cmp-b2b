package di

import features.auth.domain.AuthRepository
import features.auth.domain.AuthRepositoryImpl
import features.auth.presentation.AuthViewModel
import networking.HttpClientFactory
import org.koin.dsl.module
import utils.InMemoryTokenManager
import utils.TokenManager

val appModule = module {
    single { HttpClientFactory.create() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TokenManager> { InMemoryTokenManager() }
    single { AuthViewModel(get(), get()) }
}