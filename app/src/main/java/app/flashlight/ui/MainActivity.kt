package app.flashlight.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.flashlight.ui.theme.ComposeFlashlightTheme
import app.flashlight.event.AppEvent
import app.flashlight.event.EventManager
import app.flashlight.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var eventManager: EventManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            ComposeFlashlightTheme {
                NavGraph(navController)
            }
            collectEvents(navController)
        }
    }

    private fun ComponentActivity.collectEvents(navController: NavHostController) {
        lifecycleScope.launchWhenStarted {
            eventManager.events.collect { appEvent ->
                when (appEvent) {
                    is AppEvent.StartIntent -> startActivity(appEvent.intent)
                    is AppEvent.Navigate -> navController.navigate(appEvent.destination.route)
                    is AppEvent.TextMessage -> Toast
                        .makeText(this@MainActivity, appEvent.message, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}
