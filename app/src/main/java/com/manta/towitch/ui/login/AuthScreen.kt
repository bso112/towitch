package com.manta.towitch.ui.login

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.manta.towitch.BuildConfig
import java.util.*

@Composable
fun AuthScreen() {
    val context = LocalContext.current

    val webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val url = request?.url ?: return false
            if (url.host == "towitch") {
                val intent = Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME) // IntentURI 처리
                val uri = Uri.parse(intent.dataString)
                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                return true
            }
            return false
        }
    }

    val logInUrl = "https://id.twitch.tv/oauth2/authorize"
        .plus("?response_type=token")
        .plus("&client_id=${BuildConfig.TWITCH_CLIENT_ID}")
        .plus("&redirect_uri=https://towitch/home")
        .plus("&scope=user:read:follows")

    AndroidView(
        factory = {
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.webViewClient = webViewClient
                settings.javaScriptEnabled = true
            }
        }, update = {
            it.loadUrl(logInUrl)
        }
    )
}