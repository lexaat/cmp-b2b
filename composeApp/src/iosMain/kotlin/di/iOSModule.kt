package di

import core.i18n.LocaleStorage
import data.storage.SecureStorage
import data.storage.SecureStorageFactory
import org.koin.dsl.module

val iosModule = module {
    single<SecureStorage> {
        val storage = SecureStorageFactory().create()
        LocaleStorage.init(storage)
        storage
    }
}