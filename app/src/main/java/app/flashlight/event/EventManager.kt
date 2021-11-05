package app.flashlight.event

import android.content.Intent
import app.flashlight.navigation.NavDest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventManager @Inject constructor() {

    private val _events = MutableSharedFlow<AppEvent>(extraBufferCapacity = 1)
    val events = _events.asSharedFlow()

    fun navigateTo(destination: NavDest) {
        _events.tryEmit(AppEvent.Navigate(destination))
    }

    fun showMessage(message: String) {
        _events.tryEmit(AppEvent.TextMessage(message))
    }

    fun startIntent(intent: Intent) {
        _events.tryEmit(AppEvent.StartIntent(intent))
    }
}
