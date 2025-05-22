package di

import org.koin.core.context.startKoin

fun initKoinIOS() {
    startKoin {
        modules(
            appModule,
            configModule,
            homeModule,
            profileModule)
    }
}