package com.manta.towitch.utils

import android.util.Log

object Logger {
    private const val tag = "myLog"

    fun d(msg : String){
        Log.d(tag, msg)
    }
}