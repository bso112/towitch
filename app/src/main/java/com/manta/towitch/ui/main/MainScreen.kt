package com.manta.towitch.ui.main

import HostScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.manta.towitch.common.PreferenceHelper
import com.manta.towitch.ui.login.AuthScreen
import com.manta.towitch.ui.login.LoginScreen
import com.manta.towitch.utils.toSafe


@Composable
fun MainScreen(preferenceHelper: PreferenceHelper) {
    val navController = rememberNavController()
    val navigator: Navigator = { navController.navigate(it) }


    val initialRoute =
        if (preferenceHelper.twitchAppToken.isBlank() || preferenceHelper.twitchUserToken.isBlank()) {
            NavScreen.Login.route
        } else {
            NavScreen.Home.route
        }
    NavHost(navController = navController, startDestination = initialRoute) {
        composable(
            NavScreen.Home.route,
            deepLinks = listOf(navDeepLink { uriPattern = "https://towitch.page.link/home" })
        ) {
            HostScreen()
        }
        composable(NavScreen.Login.route) {
            LoginScreen(navigator)
        }
        composable(NavScreen.Auth.route) {
            AuthScreen()
        }
    }
}

const val ARGUMENT_INITIAL_URL = "ARGUMENT_INITIAL_URL"

typealias Navigator = (String) -> Unit

sealed class NavScreen(val route: String) {
    object Home : NavScreen("Home")
    object Login : NavScreen("Login")
    object Auth : NavScreen("Auth")

}
