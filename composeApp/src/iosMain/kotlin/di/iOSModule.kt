package di

import app.cash.sqldelight.db.SqlDriver
import core.i18n.LocaleStorage
import data.storage.SecureStorage
import data.storage.SecureStorageFactory
import org.koin.dsl.module
import database.AppDatabase
import core.DatabaseDriverFactory

val iosModule = module {
    single<SecureStorage> {
        val storage = SecureStorageFactory().create()
        LocaleStorage.init(storage)
        storage
    }

    single<SqlDriver> {
        DatabaseDriverFactory().create()
    }

    single {
        AppDatabase(get())
    }
}