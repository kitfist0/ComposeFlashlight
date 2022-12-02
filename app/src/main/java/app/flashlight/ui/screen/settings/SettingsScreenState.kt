package app.flashlight.ui.screen.settings

import app.flashlight.BuildConfig
import app.flashlight.R
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class SettingsScreenState(
    val titleStringRes: Int = R.string.app_name,
    val versionNameText: String = "ver.${BuildConfig.VERSION_NAME}",
    val settingsItems: List<SettingsItem> = SettingsItem.values().toList(),
    val longToastEvent: StateEventWithContent<String> = consumed(),
    val viewIntentEvent: StateEventWithContent<String> = consumed(),
)
