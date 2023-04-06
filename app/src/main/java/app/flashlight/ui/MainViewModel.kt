package app.flashlight.ui

import androidx.lifecycle.ViewModel
import app.flashlight.data.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStoreManager: DataStoreManager,
): ViewModel() {
    val darkTheme = dataStoreManager.darkThemeEnabled
}
