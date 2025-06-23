package push

import org.koin.dsl.module

fun pushModule() = module {
    single<PushTokenProvider> { IOSPushTokenProvider() }
}