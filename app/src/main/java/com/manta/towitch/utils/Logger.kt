package com.manta.towitch.utils

import android.util.Log
import com.manta.towitch.BuildConfig

object Logger {
    private const val tag = "towitch_logger"

    fun d(msg: String) {
        runOnDebug {
            Log.d(tag, msg)
        }
    }

    fun e(throwable: Throwable) {
        runOnDebug {
            Log.e(tag, throwable.message, throwable)
        }
    }

    fun w(msg: String) {
        runOnDebug {
            Log.w(tag, msg)
        }
    }

    fun runOnDebug(block: () -> Unit) {
        if (BuildConfig.DEBUG) {
            block()
        }
    }
}