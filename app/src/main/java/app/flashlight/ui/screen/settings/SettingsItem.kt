package app.flashlight.ui.screen.settings

import app.flashlight.R

enum class SettingsItem(
    val iconRes: Int,
    val titleRes: Int,
) {
    THEME(R.drawable.ic_twotone_palette, R.string.theme),
    GITHUB(R.drawable.ic_twotone_github, R.string.github),
    COPYRIGHT(R.drawable.ic_twotone_copyright, R.string.copyright),
}
