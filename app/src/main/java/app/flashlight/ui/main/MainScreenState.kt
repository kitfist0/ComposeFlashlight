package app.flashlight.ui.main

import android.util.DisplayMetrics
import androidx.compose.foundation.lazy.LazyListState
import app.flashlight.data.DataConstants

data class MainScreenState(
    val modes: List<Int> = DataConstants.MODES.toList(),
    val selectedMode: Int = DataConstants.DEFAULT_MODE_ID,
    val isEnabled: Boolean = false
) {
    companion object {
        // Maximum number of visible list items
        private const val MAX_NUM_OF_VISIBLE_ITEMS = 5
        private var previousFirstItemIndex = -1

        fun MainScreenState.getFirstIndex() = Int.MAX_VALUE / 2 + selectedMode - 2

        fun DisplayMetrics.getItemSize() = widthPixels / MAX_NUM_OF_VISIBLE_ITEMS / density

        fun LazyListState.centralVisibleIndex() =
            firstVisibleItemIndex + MAX_NUM_OF_VISIBLE_ITEMS / 2

        fun LazyListState.setOnScrollFinishedListener(
            onScrollFinished: (firstIndex: Int, selectedMode: Int) -> Unit
        ) {
            if (!isScrollInProgress && previousFirstItemIndex != firstVisibleItemIndex) {
                previousFirstItemIndex = firstVisibleItemIndex
                val mode = centralVisibleIndex() % DataConstants.MODES.size
                onScrollFinished.invoke(firstVisibleItemIndex, mode)
            }
        }
    }
}