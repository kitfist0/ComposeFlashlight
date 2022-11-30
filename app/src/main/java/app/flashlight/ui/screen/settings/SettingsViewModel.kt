package app.flashlight.ui.screen.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import app.flashlight.BuildConfig
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _screenState = MutableStateFlow(SettingsScreenState())
    val screenState = _screenState.asStateFlow()

    private var state: SettingsScreenState
        get() = _screenState.value
        set(newState) {
            _screenState.update { newState }
        }

    fun onThemeItemClicked() {
        state = state.copy(longToastEvent = triggered("In developing ʕ•ᴥ•ʔ"))
    }

    fun onConsumedToastEvent() {
        state = state.copy(longToastEvent = consumed())
    }

    fun onGitHubItemClicked() {
        state = state.copy(viewIntentEvent = triggered(BuildConfig.GITHUB))
    }

    fun onLicenseItemClicked() {
        state = state.copy(viewIntentEvent = triggered(BuildConfig.LICENSE))
    }

    fun onConsumedViewIntentEvent() {
        state = state.copy(viewIntentEvent = consumed())
    }
}
