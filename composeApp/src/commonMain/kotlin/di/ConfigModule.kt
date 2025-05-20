package di

import config.Config
import org.koin.core.qualifier.named
import org.koin.dsl.module

val configModule = module {

    single(named("baseUrl")) { Config.current.baseUrl }
    single(named("timeout")) { Config.current.timeoutSeconds }
    single(named("logLevel")) { Config.current.logLevel }
    single(named("isMockEnabled")) { Config.current.isMockEnabled }
}