package app.flashlight.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.flashlight.core.*
import app.flashlight.core.DataStoreManager
import app.flashlight.ui.base.BaseViewModel
import app.flashlight.ui.main.MainScreenState
import app.flashlight.ui.settings.KEY_GITHUB_ITEM
import app.flashlight.ui.settings.KEY_LICENSE_ITEM
import app.flashlight.ui.settings.KEY_THEME_ITEM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.content.Intent
import android.net.Uri
import app.flashlight.BuildConfig
import app.flashlight.ui.base.NavDest

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val flashlight: Flashlight,
    private val dataStoreManager: DataStoreManager,
) : BaseViewModel() {

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

    fun onSettingsButtonClicked() {
        navigateTo(NavDest.SETTINGS)
    }

    fun onSettingsItemClicked(itemKey: String) {
        Log.d(TAG, "item $itemKey clicked")
        when (itemKey) {
            KEY_THEME_ITEM ->
                showMessage("In developing ʕ•ᴥ•ʔ")
            KEY_GITHUB_ITEM ->
                startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.GITHUB)))
            KEY_LICENSE_ITEM ->
                startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.LICENSE)))
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
