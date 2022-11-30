package app.flashlight.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.flashlight.core.*
import app.flashlight.data.DataStoreManager
import app.flashlight.ui.navigation.NavDest
import dagger.hilt.android.lifecycle.HiltViewModel
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val flashlight: Flashlight,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState = _screenState.asStateFlow()

    private var state: HomeScreenState
        get() = _screenState.value
        set(newState) {
            _screenState.update { newState }
        }

    init {
        combine(dataStoreManager.mode, dataStoreManager.flashlightEnabled) { mode, enabled ->
            mode to enabled
        }.onEach {
            state = state.copy(
                selectedMode = it.first,
                switchChecked = it.second,
            )
        }.launchIn(viewModelScope)
    }

    fun onSettingsButtonClicked() {
        state = state.copy(navigationEvent = triggered(NavDest.SETTINGS))
    }

    fun onConsumedNavigationEvent() {
        state = state.copy(navigationEvent = consumed())
    }

    fun onModeSelected(mode: Int) {
        viewModelScope.launch {
            dataStoreManager.setMode(mode)
            flashlight.setMode(mode)
        }
    }

    fun onSwitchCheckedChanged(checked: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setFlashlightEnabled(checked)
            flashlight.toggle(checked)
        }
    }
}
