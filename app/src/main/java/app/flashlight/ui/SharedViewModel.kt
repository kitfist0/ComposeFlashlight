package app.flashlight.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.flashlight.data.DataStoreManager
import app.flashlight.ui.main.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
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
                dataStoreManager.isEnabled
            ) { mode, enabled ->
                MainScreenState(
                    selectedMode = mode,
                    isEnabled = enabled
                )
            }.collect { _mainScreenState.value = it }
        }
    }

    fun modeSelected(mode: Int) {
        Log.d(TAG, "mode $mode selected")
        viewModelScope.launch { dataStoreManager.setMode(mode) }
    }

    fun switchStateChanged(checked: Boolean) {
        Log.d(TAG, "switch state is $checked")
        viewModelScope.launch { dataStoreManager.setIsEnabled(checked) }
    }

    companion object {
        private const val TAG = "MAIN_VIEW_MODEL"
    }
}