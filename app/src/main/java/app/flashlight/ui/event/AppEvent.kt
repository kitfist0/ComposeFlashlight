package app.flashlight.ui.event

import android.content.Intent
import app.flashlight.ui.navigation.NavDest

sealed class AppEvent {
    data class Navigate(val destination: NavDest) : AppEvent()
    data class TextMessage(val message: String) : AppEvent()
    data class StartIntent(val intent: Intent) : AppEvent()
}
