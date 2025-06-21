package di

import org.koin.dsl.module
import platform.PushService
import platform.iOSPushService

actual fun platformPushModule() = module {
    single<PushService> { iOSPushService() }
}