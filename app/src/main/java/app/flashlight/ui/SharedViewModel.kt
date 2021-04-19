package app.flashlight.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.flashlight.core.*
import app.flashlight.core.DataStoreManager
import app.flashlight.ui.main.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val flashlight: Flashlight,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    // Holds our view state which the UI collects via [state]
    private val _mainScreenState = MutableStateFlow(MainScreenState())
    val mainScreenState: StateFlow<MainScreenState>
        get() = _mainScreenState

    init {
        Log.d(TAG, "init")
        viewModelScope.launch {
            combine(
                dataStoreManager.mode,
                dataStoreManager.flashlightEnabled
            ) { mode, enabled ->
                MainScreenState(
                    selectedMode = mode,
                    switchChecked = enabled,
                )
            }.distinctUntilChanged().collect {
                _mainScreenState.value = it
            }
        }
    }

    fun onModeSelected(mode: Int) = viewModelScope.launch {
        Log.d(TAG, "mode $mode selected")
        dataStoreManager.setMode(mode)
        flashlight.setMode(mode)
    }

    fun onSwitchCheckedChanged(checked: Boolean) = viewModelScope.launch {
        Log.d(TAG, "switch ${if (checked) "on" else "off"}")
        dataStoreManager.setFlashlightEnabled(checked)
        flashlight.toggle(checked)
    }

    companion object {
        private const val TAG = "MAIN_VIEW_MODEL"
    }
}