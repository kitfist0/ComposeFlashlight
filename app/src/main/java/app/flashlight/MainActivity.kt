package app.flashlight

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.flashlight.ui.SharedViewModel
import app.flashlight.ui.main.MainScreen
import app.flashlight.ui.settings.SettingsScreen
import app.flashlight.ui.theme.ComposeFlashlightTheme
import app.flashlight.ui.base.AppEvent
import app.flashlight.ui.base.NavDest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            ComposeFlashlightTheme {
                NavHost(navController, startDestination = NavDest.MAIN.route) {
                    composable(NavDest.MAIN.route) {
                        MainScreen(
                            viewModel = sharedViewModel,
                            onSettingsClick = { sharedViewModel.onSettingsButtonClicked() },
                        )
                    }
                    composable(NavDest.SETTINGS.route) {
                        SettingsScreen(
                            viewModel = sharedViewModel,
                        )
                    }
                }
            }

            lifecycleScope.launchWhenStarted {
                sharedViewModel.events.collect { appEvent ->
                    when (appEvent) {
                        is AppEvent.StartIntent -> startActivity(appEvent.intent)
                        is AppEvent.Navigate -> navController.navigate(appEvent.destination.route)
                        is AppEvent.TextMessage -> Toast
                            .makeText(this@MainActivity, appEvent.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}
