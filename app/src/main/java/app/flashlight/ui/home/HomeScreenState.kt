package app.flashlight.ui.home

import app.flashlight.core.DataConstants

data class HomeScreenState(
    val modes: List<Int> = DataConstants.MODES.toList(),
    val selectedMode: Int = DataConstants.DEFAULT_MODE,
    val switchChecked: Boolean = false,
)
