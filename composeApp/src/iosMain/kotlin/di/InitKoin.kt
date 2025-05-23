package di

import org.koin.core.context.startKoin

fun initKoinIOS() {
    startKoin {
        modules(
            clientModule,
            configModule,
            appModule,
            homeModule,
            profileModule)
    }
}