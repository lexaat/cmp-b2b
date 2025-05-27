package di

import android.content.Context
import core.i18n.LocaleStorage
import data.storage.SecureStorageFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
    single {
        val context: Context = androidContext()
        val storage = SecureStorageFactory(context).create()
        LocaleStorage.init(storage)
        storage
    }
}