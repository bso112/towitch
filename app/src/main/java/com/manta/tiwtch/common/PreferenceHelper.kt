package com.manta.tiwtch.common

import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PreferenceHelper @Inject constructor(
    private val sharedPreference: SharedPreferences
) {

    fun getTwitchAccessToken() = sharedPreference.getString(Consts.TWITCH_ACCESS_TOKEN, "")

    fun setTwitchAccessToken(accessToken: String) {
        sharedPreference.edit().putString(Consts.TWITCH_ACCESS_TOKEN, accessToken).apply()
    }


}