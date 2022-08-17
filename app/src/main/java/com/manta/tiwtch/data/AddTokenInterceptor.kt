package com.manta.tiwtch.data

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.manta.tiwtch.BuildConfig
import com.manta.tiwtch.common.Consts
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AddTokenInterceptor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                ("Bearer " + sharedPreferences.getString(Consts.TWITCH_ACCESS_TOKEN, ""))
            )
            .addHeader("Client-id", BuildConfig.TWITCH_CLIENT_ID)
            .build()
        Log.d("okHttp", "header: " + request.headers.toString())
        return chain.proceed(request)
    }

}
