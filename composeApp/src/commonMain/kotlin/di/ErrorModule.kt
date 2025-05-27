package di

import core.error.ApiErrorHandler
import core.error.DefaultErrorHandler
import core.presentation.BaseSideEffect
import core.session.LogoutManager
import org.koin.dsl.module

val errorModule = module {
    single { LogoutManager(get()) }
    single<ApiErrorHandler<BaseSideEffect>> {
        DefaultErrorHandler(
            authRepository = get(),
            logoutManager = get(),
            tokenManager = get()
        )
    }
}