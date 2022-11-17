package com.josycom.mayorjay.genpass.util

import android.util.Log
import com.josycom.mayorjay.genpass.BuildConfig
import timber.log.Timber

object AppLogger {

    fun init() = Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else ReleaseTree())

class ReleaseTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }

        when (priority) {
            Log.ERROR -> {
                //Log.e(tag, message, t)
            }
            Log.WARN -> {
                //Log.w(tag, message)
            }
        }
    }
}
}