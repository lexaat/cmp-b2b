package push

import org.koin.dsl.module
import uz.hb.push_core.PushTokenProvider

fun pushModule(context: android.content.Context) = module {
    single<PushTokenProvider> { AndroidPushTokenProvider(context) }
}