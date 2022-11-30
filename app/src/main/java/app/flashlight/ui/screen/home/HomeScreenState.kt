package app.flashlight.ui.screen.home

import app.flashlight.data.DataConstants
import app.flashlight.ui.navigation.NavDest
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class HomeScreenState(
    val modes: List<Int> = DataConstants.MODES.toList(),
    val selectedMode: Int = DataConstants.DEFAULT_MODE,
    val switchChecked: Boolean = false,
    val navigationEvent: StateEventWithContent<NavDest> = consumed(),
)
