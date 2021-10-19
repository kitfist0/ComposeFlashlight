package app.flashlight.ui.base

import android.content.Intent

sealed class AppEvent {
    data class Navigate(val destination: NavDest) : AppEvent()
    data class TextMessage(val message: String) : AppEvent()
    data class StartIntent(val intent: Intent) : AppEvent()
}
