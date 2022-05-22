package com.josycom.mayorjay.genpass.util

import android.util.Log
import timber.log.Timber

class Logger : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

        when (priority) {
            Log.ERROR -> {
                Log.e(tag, message, t)
            }
            Log.WARN -> {
                Log.w(tag, message)
            }
            Log.INFO -> {
                Log.i(tag, message)
            }
        }
    }
}