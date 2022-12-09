package com.manta.towitch.utils


val defaultExceptionHandler: ExceptionHandler = {
    Logger.e(it)
}