package di


import org.koin.dsl.module
import platform.AndroidPushService
import platform.PushService

actual fun platformPushModule() = module {
    single<PushService> { AndroidPushService() }
}