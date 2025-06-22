package uz.hb.b2b.notifications_core.push

import org.koin.dsl.module

actual val platformPushModule = module {
    single<PushService> { AndroidPushService() }
}