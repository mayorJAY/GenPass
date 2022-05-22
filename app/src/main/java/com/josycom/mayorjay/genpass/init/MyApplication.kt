package com.josycom.mayorjay.genpass.init

import android.app.Application
import com.josycom.mayorjay.genpass.BuildConfig
import com.josycom.mayorjay.genpass.util.Logger
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(Logger())
        }
    }
}