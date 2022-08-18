package com.manta.tiwtch.common

import android.content.SharedPreferences
import com.manta.tiwtch.utils.toSafe
import javax.inject.Inject

class PreferenceHelper @Inject constructor(
    private val sharedPreference: SharedPreferences
) {
    var twitchAppToken: String
        get() = formatAsToken(sharedPreference.getString(Consts.TWITCH_APP_TOKEN, "").toSafe())
        set(value) {
            sharedPreference.edit().putString(Consts.TWITCH_APP_TOKEN, value).apply()
        }

    var twitchUserToken: String
        get() = formatAsToken(sharedPreference.getString(Consts.TWITCH_USER_TOKEN, "").toSafe())
        set(value) {
            sharedPreference.edit().putString(Consts.TWITCH_USER_TOKEN, value).apply()
        }


    private fun formatAsToken(token: String): String {
        return if (token.isBlank()) {
            ""
        } else {
            "Bearer " + sharedPreference.getString(Consts.TWITCH_APP_TOKEN, "").toSafe()
        }
    }

}