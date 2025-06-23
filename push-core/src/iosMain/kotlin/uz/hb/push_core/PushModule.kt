package push

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun pushModule(appContext: Any?): Module = module {
    single<PushTokenProvider> { IOSPushTokenProvider() }
}