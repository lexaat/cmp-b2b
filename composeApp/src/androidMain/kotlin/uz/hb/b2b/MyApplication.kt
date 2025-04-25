package uz.hb.b2b

import android.app.Application
import di.initKoinAndroid

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinAndroid()
    }
}