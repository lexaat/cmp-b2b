package di

import org.koin.core.context.startKoin

fun initKoinAndroid() {
    startKoin {
        modules(
            clientModule,configModule, appModule, homeModule)
    }
}