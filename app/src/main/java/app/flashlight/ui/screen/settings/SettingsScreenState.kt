package app.flashlight.ui.screen.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.flashlight.BuildConfig
import app.flashlight.R
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class SettingItemState(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
)

data class SingleChoiceSheetState<T>(
    val selectedValue: T,
    val allValues: List<T>,
)

data class SettingsScreenState(
    @StringRes val titleStringRes: Int = R.string.app_name,
    val versionNameText: String = "ver.${BuildConfig.VERSION_NAME}",
    val themeSettingItem: SettingItemState = SettingItemState(
        R.drawable.ic_twotone_light_mode, R.string.settings_switch_to_dark_mode,
    ),
    val shutdownTimeoutItem: SettingItemState = SettingItemState(
        R.drawable.ic_twotone_alarm, R.string.settings_shutdown_timeout,
    ),
    val githubSettingItem: SettingItemState = SettingItemState(
        R.drawable.ic_twotone_github, R.string.settings_source_code,
    ),
    val policySettingItem: SettingItemState = SettingItemState(
        R.drawable.ic_twotone_policy, R.string.privacy_policy,
    ),
    val timeoutBottomSheetEvent: StateEventWithContent<SingleChoiceSheetState<Long>> = consumed(),
    val longToastEvent: StateEventWithContent<String> = consumed(),
    val viewIntentEvent: StateEventWithContent<String> = consumed(),
)
