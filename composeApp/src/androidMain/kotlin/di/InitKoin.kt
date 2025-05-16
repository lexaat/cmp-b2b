package di

import org.koin.core.context.startKoin

fun initKoinAndroid() {
    startKoin {
        modules(appModule, configModule)
    }
}