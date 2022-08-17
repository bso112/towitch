package com.manta.tiwtch.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.manta.tiwtch.ui.home.HomeScreen


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navigator: Navigator = { navController.navigate(it) }


    NavHost(navController = navController, startDestination = NavScreen.Home.route) {
        composable(NavScreen.Home.route) {
            HomeScreen()
        }
    }
}

typealias Navigator = (String)->Unit

sealed class NavScreen(val route: String) {
    object Home : NavScreen("Home")
}
