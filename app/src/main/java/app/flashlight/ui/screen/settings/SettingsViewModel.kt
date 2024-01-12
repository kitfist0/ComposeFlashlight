package app.flashlight.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import app.flashlight.BuildConfig
import app.flashlight.R
import app.flashlight.data.DataStoreManager
import app.flashlight.data.Timeout
import de.palm.composestateevents.consumed
import de.palm.composestateevents.triggered
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _screenState = MutableStateFlow(SettingsScreenState())
    val screenState = _screenState.asStateFlow()

    private var state: SettingsScreenState
        get() = _screenState.value
        set(newState) {
            _screenState.update { newState }
        }

    init {
        dataStoreManager.darkThemeEnabled
            .onEach { isEnabled ->
                val settingsItem = SettingItemState(
                    if (isEnabled) R.drawable.ic_twotone_light_mode else R.drawable.ic_twotone_dark_mode,
                    if (isEnabled) R.string.settings_switch_to_light_mode else R.string.settings_switch_to_dark_mode,
                )
                state = state.copy(themeSettingItem = settingsItem)
            }
            .launchIn(viewModelScope)
    }

    fun onSettingItemClicked(itemId: SettingItemId) {
        when (itemId) {
            SettingItemId.THEME -> viewModelScope.launch {
                val darkThemeEnabled = dataStoreManager.darkThemeEnabled.first()
                dataStoreManager.setDarkThemeEnabled(!darkThemeEnabled)
            }
            SettingItemId.TIMEOUT ->
                viewModelScope.launch {
                    val selectedValue = dataStoreManager.shutdownTimeout.first().valueInMinutes
                    val allValues = Timeout.entries.map { it.valueInMinutes }
                    state = state.copy(
                        timeoutBottomSheetEvent = triggered(SingleChoiceSheetState(selectedValue, allValues))
                    )
                }
            SettingItemId.GITHUB ->
                state = state.copy(viewIntentEvent = triggered(BuildConfig.GITHUB))
            SettingItemId.POLICY ->
                state = state.copy(viewIntentEvent = triggered(BuildConfig.POLICY))
        }
    }

    fun onTimeoutValueSelected(timeout: Long) {
        viewModelScope.launch {
            Timeout.entries.find { it.valueInMinutes == timeout }
                ?.let { dataStoreManager.setShutdownTimeout(it) }
        }
    }

    fun onConsumedTimeoutBottomSheetEvent() {
        state = state.copy(timeoutBottomSheetEvent = consumed())
    }

    fun onConsumedToastEvent() {
        state = state.copy(longToastEvent = consumed())
    }

    fun onConsumedViewIntentEvent() {
        state = state.copy(viewIntentEvent = consumed())
    }
}
