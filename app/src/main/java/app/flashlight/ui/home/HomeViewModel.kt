package app.flashlight.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.flashlight.core.*
import app.flashlight.core.DataStoreManager
import app.flashlight.event.EventManager
import app.flashlight.navigation.NavDest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val flashlight: Flashlight,
    private val dataStoreManager: DataStoreManager,
    private val eventManager: EventManager,
) : ViewModel() {

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState: StateFlow<HomeScreenState>
        get() = _homeScreenState

    init {
        viewModelScope.launch {
            combine(
                dataStoreManager.mode,
                dataStoreManager.flashlightEnabled
            ) { mode, enabled ->
                HomeScreenState(
                    selectedMode = mode,
                    switchChecked = enabled,
                )
            }.distinctUntilChanged().collect {
                _homeScreenState.value = it
            }
        }
    }

    fun onSettingsButtonClicked() {
        eventManager.navigateTo(NavDest.SETTINGS)
    }

    fun onModeSelected(mode: Int) = viewModelScope.launch {
        dataStoreManager.setMode(mode)
        flashlight.setMode(mode)
    }

    fun onSwitchCheckedChanged(checked: Boolean) = viewModelScope.launch {
        dataStoreManager.setFlashlightEnabled(checked)
        flashlight.toggle(checked)
    }
}