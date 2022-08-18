package com.manta.tiwtch.ui.main

import android.content.Intent
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
import com.manta.tiwtch.ui.theme.TiwtchTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

    }
    //https://tiwtch.page.link/#access_token=ugdulgd0je9fkjxxpqrpudlpt2ltte&scope=user%3Aread%3Afollows&state=40907809-282f-43af-a68c-564bb1f9a557&token_type=bearer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TiwtchTheme {
                MainScreen()
            }
        }
    }
}

