package di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

fun initKoinAndroid(context: Context) {
    startKoin {
        androidContext(context)
        modules(
            androidModule,
            clientModule,
            configModule,
            appModule,
            homeModule,
            profileModule,
            errorModule
        )
    }
}