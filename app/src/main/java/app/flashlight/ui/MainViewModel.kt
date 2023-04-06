package app.flashlight.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.flashlight.data.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
): ViewModel() {
    val darkTheme = dataStoreManager.darkThemeEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = true
    )
}
