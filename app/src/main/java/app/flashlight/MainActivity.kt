package app.flashlight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.flashlight.ui.SharedViewModel
import app.flashlight.ui.main.MainScreen
import app.flashlight.ui.settings.SettingsScreen
import app.flashlight.ui.theme.ComposeFlashlightTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeFlashlightTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = MAIN_DEST) {
                    composable(MAIN_DEST) {
                        MainScreen(
                            viewModel = sharedViewModel,
                            onSettingsClick = { navController.navigate(SETTINGS_DEST) },
                        )
                    }
                    composable(SETTINGS_DEST) {
                        SettingsScreen()
                    }
                }
            }
        }
    }

    companion object {
        const val MAIN_DEST = "main"
        const val SETTINGS_DEST = "settings"
    }
}