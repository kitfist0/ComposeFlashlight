package app.flashlight.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.flashlight.ui.screen.home.HomeScreen
import app.flashlight.ui.screen.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavDest.HOME.route) {
        composable(NavDest.HOME.route) {
            HomeScreen(navController, hiltViewModel())
        }
        composable(NavDest.SETTINGS.route) {
            SettingsScreen(hiltViewModel())
        }
    }
}
