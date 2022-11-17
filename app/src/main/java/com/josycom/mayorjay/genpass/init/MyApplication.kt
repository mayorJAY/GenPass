package com.josycom.mayorjay.genpass.init

import android.app.Application
import com.josycom.mayorjay.genpass.util.AppLogger

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppLogger.init()
    }
}