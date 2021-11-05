package app.flashlight.ui.settings

import androidx.annotation.StringRes

data class SettingsScreenState(
    @StringRes
    val titleStringRes: Int,
    val versionNameText: String,
)
