package di

import core.session.LogoutManager
import org.koin.dsl.module

val errorModule = module {
    single { LogoutManager(get()) }
}