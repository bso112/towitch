package com.manta.towitch.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.manta.towitch.common.PreferenceHelper
import com.manta.towitch.ui.theme.TowitchTheme
import com.manta.towitch.utils.toSafe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleDeeplink()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleDeeplink()
        setContent {
            TowitchTheme {
                MainScreen(preferenceHelper)
            }
        }
    }

    // http://localhost:3000#access_token=*** -> http://localhost:3000?access_token=***
    private fun handleDeeplink() {
        intent.dataString?.replace("#", "?")
            ?.let {
                Uri.parse(it)
            }?.apply {
                preferenceHelper.twitchUserToken = getQueryParameter("access_token").toSafe()
            }
    }
}

