package app.flashlight.ui.screen.settings

import app.flashlight.R

enum class SettingsItem(
    val iconRes: Int,
    val titleRes: Int,
) {
    THEME(R.drawable.ic_twotone_palette, R.string.theme),
    GITHUB(R.drawable.ic_twotone_github, R.string.github),
    POLICY(R.drawable.ic_twotone_policy, R.string.privacy_policy),
}
