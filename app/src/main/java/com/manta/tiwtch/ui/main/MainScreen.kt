package com.manta.tiwtch.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.manta.tiwtch.common.PreferenceHelper
import com.manta.tiwtch.ui.home.HomeScreen
import com.manta.tiwtch.ui.login.LoginScreen


@Composable
fun MainScreen(preferenceHelper: PreferenceHelper) {
    val navController = rememberNavController()
    val navigator: Navigator = { navController.navigate(it) }

    val initialRoute = if(preferenceHelper.twitchAppToken.isBlank() || preferenceHelper.twitchUserToken.isBlank()){
        NavScreen.Login.route
    }else{
        NavScreen.Home.route
    }
    NavHost(navController = navController, startDestination =  NavScreen.Login.route) {
        composable(
            NavScreen.Home.route,
            deepLinks = listOf(navDeepLink { uriPattern = "https://tiwtch.page.link/home" })
        ) {
            HomeScreen()
        }
        composable(NavScreen.Login.route) {
            LoginScreen()
        }
    }
}

typealias Navigator = (String) -> Unit

sealed class NavScreen(val route: String) {
    object Home : NavScreen("Home")
    object Login : NavScreen("Login")
}
