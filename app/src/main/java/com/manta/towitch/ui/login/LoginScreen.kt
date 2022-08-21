package com.manta.towitch.ui.login

import android.content.Intent
import android.net.Uri
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.manta.towitch.BuildConfig
import java.util.*

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Button(onClick = {
        loginViewModel.fetchTwitchAppToken()

        val logInUrl = "https://id.twitch.tv/oauth2/authorize"
            .plus("?response_type=token")
            .plus("&client_id=${BuildConfig.TWITCH_CLIENT_ID}")
            .plus("&redirect_uri=https://towitch.page.link/home")
            .plus("&scope=user:read:follows")
            .plus("&state=${UUID.randomUUID()}") //TODO

        Intent(Intent.ACTION_VIEW, Uri.parse(logInUrl)).also {
            context.startActivity(it)
        }
    }) {
        Text("login!")
    }
}