package com.manta.tiwtch.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(mainViewModel: HomeViewModel = hiltViewModel()) {
    val streamList = mainViewModel.streams.collectAsState()

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(streamList.value) { stream ->
                Text(stream.title)
            }

        }
    }
}