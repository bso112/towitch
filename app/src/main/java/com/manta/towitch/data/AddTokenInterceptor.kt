package com.manta.towitch.data

import android.content.SharedPreferences
import com.manta.towitch.BuildConfig
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
