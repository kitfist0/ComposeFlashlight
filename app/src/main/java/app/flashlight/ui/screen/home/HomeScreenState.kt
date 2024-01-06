package app.flashlight.ui.screen.home

import android.util.DisplayMetrics
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Size
import app.flashlight.data.Mode
import app.flashlight.ui.navigation.NavDest
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class HomeScreenState(
    val modesOrdinals: List<Int> = Mode.entries.map { it.ordinal },
    val selectedMode: Mode = Mode.DEFAULT_MODE,
    val switchChecked: Boolean = false,
    val navigationEvent: StateEventWithContent<NavDest> = consumed(),
) {
    companion object {
        // The number of modes simultaneously displayed on the user's screen
        private const val NUM_OF_SIMULTANEOUSLY_DISPLAYED_MODES = 5

        const val LIST_SIZE = Int.MAX_VALUE

        fun HomeScreenState.getListInitialIndex(): Int {
            return LIST_SIZE / 2 + selectedMode.ordinal - NUM_OF_SIMULTANEOUSLY_DISPLAYED_MODES / 2
        }

        fun DisplayMetrics.getSizeOfListItem(): Size {
            val width = widthPixels / NUM_OF_SIMULTANEOUSLY_DISPLAYED_MODES / density
            return Size(width, 1.5f * width)
        }

        fun LazyListState.getListCentralVisibleIndex(): Int {
            return firstVisibleItemIndex + NUM_OF_SIMULTANEOUSLY_DISPLAYED_MODES / 2
        }

        fun LazyListState.getSelectedMode(): Mode {
            val ordinal = getListCentralVisibleIndex() % Mode.entries.size
            return Mode.entries[ordinal]
        }
    }
}
