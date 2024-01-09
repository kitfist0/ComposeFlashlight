package app.flashlight.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.flashlight.core.Flashlight
import app.flashlight.data.DataStoreManager
import app.flashlight.data.Mode
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
        flashlight.start()
        dataStoreManager.flashlightEnabled
            .onEach { state = state.copy(switchChecked = it) }
            .launchIn(viewModelScope)
        dataStoreManager.mode
            .onEach { state = state.copy(selectedMode = it) }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        flashlight.stop()
        super.onCleared()
    }

    fun onSettingsButtonClicked() {
        state = state.copy(navigationEvent = triggered(NavDest.SETTINGS))
    }

    fun onConsumedNavigationEvent() {
        state = state.copy(navigationEvent = consumed())
    }

    fun onModeSelected(mode: Mode) {
        viewModelScope.launch {
            dataStoreManager.setMode(mode)
        }
    }

    fun onSwitchCheckedChanged(checked: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setFlashlightEnabled(checked)
        }
    }
}
