package di

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import core.DatabaseDriverFactory
import core.i18n.LocaleStorage
import data.storage.SecureStorageFactory
import database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val androidModule = module {
    single {
        val context: Context = androidContext()
        val storage = SecureStorageFactory(context).create()
        LocaleStorage.init(storage)
        storage
    }

    single { DatabaseDriverFactory(get()) } // get<Context>()
    single { get<DatabaseDriverFactory>().create() } // SqlDriver

    single {
        val context: Context = get()
        AndroidSqliteDriver(AppDatabase.Schema, context, "app.db")
    }
}