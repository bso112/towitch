package com.manta.tiwtch.data

import android.content.SharedPreferences
import android.util.Log
import com.manta.tiwtch.BuildConfig
import com.manta.tiwtch.common.Consts
import okhttp3.Interceptor
import okhttp3.Response

class AddTokenInterceptor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Client-id", BuildConfig.TWITCH_CLIENT_ID)
            .build()
        return chain.proceed(request)
    }

}
