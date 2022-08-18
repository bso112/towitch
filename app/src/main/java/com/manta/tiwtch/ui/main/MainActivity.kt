package com.manta.tiwtch.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.manta.tiwtch.common.PreferenceHelper
import com.manta.tiwtch.ui.theme.TiwtchTheme
import com.manta.tiwtch.utils.toSafe
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
            TiwtchTheme {
                MainScreen()
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

