package app.flashlight.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.flashlight.core.*
import app.flashlight.data.DataStoreManager
import app.flashlight.ui.event.EventManager
import app.flashlight.ui.navigation.NavDest
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
    val homeScreenState = _homeScreenState.asStateFlow()

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
