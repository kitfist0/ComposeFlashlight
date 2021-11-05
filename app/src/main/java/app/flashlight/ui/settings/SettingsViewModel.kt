package app.flashlight.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import app.flashlight.BuildConfig
import app.flashlight.R
import app.flashlight.event.EventManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val eventManager: EventManager,
) : ViewModel() {

    private val _settingsScreenState = MutableStateFlow(
        SettingsScreenState(
            titleStringRes = R.string.app_name,
            versionNameText = "ver.${BuildConfig.VERSION_NAME}",
        )
    )
    val settingsScreenState: StateFlow<SettingsScreenState>
        get() = _settingsScreenState

    fun onThemeItemClicked() {
        eventManager.showMessage("In developing ʕ•ᴥ•ʔ")
    }

    fun onGitHubItemClicked() {
        eventManager.startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.GITHUB)))
    }

    fun onLicenseItemClicked() {
        eventManager.startIntent(Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.LICENSE)))
    }
}
