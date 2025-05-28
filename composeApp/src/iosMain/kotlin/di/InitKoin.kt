package di

import org.koin.core.context.startKoin

fun initKoinIOS() {
    startKoin {
        modules(
            iosModule,
            clientModule,
            configModule,
            appModule,
            homeModule,
            profileModule,
            errorModule,
            bioModule()
        )
    }
}