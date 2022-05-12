package app.flashlight.ui.screen.home

import app.flashlight.data.DataConstants

data class HomeScreenState(
    val modes: List<Int> = DataConstants.MODES.toList(),
    val selectedMode: Int = DataConstants.DEFAULT_MODE,
    val switchChecked: Boolean = false,
)
