package app.flashlight.ui.base

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _events = MutableSharedFlow<AppEvent>()
    val events = _events.asSharedFlow()

    fun navigateTo(destination: NavDest) {
        invokeEvent(AppEvent.Navigate(destination))
    }

    fun showMessage(message: String) {
        invokeEvent(AppEvent.TextMessage(message))
    }

    fun startIntent(intent: Intent) {
        invokeEvent(AppEvent.StartIntent(intent))
    }

    private fun invokeEvent(event: AppEvent) {
        viewModelScope.launch { _events.emit(event) }
    }
}
