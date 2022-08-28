package com.manta.towitch.ui.login

import android.content.Intent
import android.net.Uri
import android.net.vcn.VcnConfig
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.manta.towitch.BuildConfig
import com.manta.towitch.R
import com.manta.towitch.common.VCenter
import com.manta.towitch.common.VSpacer
import com.manta.towitch.ui.theme.Black
import com.manta.towitch.ui.theme.Purple500
import com.manta.towitch.ui.theme.White
import com.manta.towitch.ui.theme.content1
import java.util.*

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = hiltViewModel()) {
    val context = LocalContext.current
    Surface(Modifier.fillMaxSize(), color = Purple500) {
        VCenter {
            Image(
                painter = painterResource(id = R.drawable.twtich_logo),
                contentDescription = "logo",
                colorFilter = ColorFilter.tint(White),
                modifier = Modifier
                    .width(300.dp)
                    .height(100.dp)
            )
            VSpacer(dp = 60.dp)
            Button(colors = ButtonDefaults.buttonColors(White), onClick = {
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
            }, shape = RoundedCornerShape(5.dp)) {
                Text("로그인", color = Purple500, fontSize= content1, fontWeight = FontWeight.Bold)
            }
        }

    }

}