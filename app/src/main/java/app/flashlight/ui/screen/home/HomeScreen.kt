package app.flashlight.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.flashlight.R
import app.flashlight.data.Mode
import app.flashlight.ui.screen.home.HomeScreenState.Companion.getListCentralVisibleIndex
import app.flashlight.ui.screen.home.HomeScreenState.Companion.getListInitialIndex
import app.flashlight.ui.screen.home.HomeScreenState.Companion.getSelectedMode
import app.flashlight.ui.screen.home.HomeScreenState.Companion.getSizeOfListItem
import de.palm.composestateevents.EventEffect
import kotlinx.coroutines.launch

private val SETTINGS_BUTTON_PADDING_TOP = 48.dp
private val SETTINGS_BUTTON_SIZE = 48.dp

private var previousFirstItemIndex = -1

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel) {

    val screenState: HomeScreenState by viewModel.screenState.collectAsState()

    HomeScreenContent(
        screenState = screenState,
        onModeSelected = { mode -> viewModel.onModeSelected(mode) },
        onSettingsClicked = { viewModel.onSettingsButtonClicked() },
        onSwitchCheckedChanged = { checked -> viewModel.onSwitchCheckedChanged(checked) },
    )

    EventEffect(
        event = screenState.navigationEvent,
        onConsumed = viewModel::onConsumedNavigationEvent,
    ) {
        navController.navigate(it.route)
    }
}

@Composable
private fun HomeScreenContent(
    screenState: HomeScreenState,
    onModeSelected: suspend (Mode) -> Unit,
    onSettingsClicked: () -> Unit,
    onSwitchCheckedChanged: (Boolean) -> Unit,
) {
    val items = CircularAdapter(screenState.modesOrdinals, HomeScreenState.LIST_SIZE)
    val itemSize = LocalContext.current.resources.displayMetrics.getSizeOfListItem()
    val lazyListState = rememberLazyListState(screenState.getListInitialIndex())
    val centralVisibleIndex = lazyListState.getListCentralVisibleIndex()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyRow(state = lazyListState) {
            itemsIndexed(items) { index, item ->
                HomeScreenItem(
                    itemSize = itemSize,
                    itemTitle = item.toString(),
                    isCentralItem = index == centralVisibleIndex,
                )
            }
            with(lazyListState) {
                if (!isScrollInProgress && previousFirstItemIndex != firstVisibleItemIndex) {
                    previousFirstItemIndex = firstVisibleItemIndex
                    scope.launch {
                        animateScrollToItem(firstVisibleItemIndex)
                        onModeSelected(getSelectedMode())
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SETTINGS_BUTTON_PADDING_TOP)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_twotone_settings),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                modifier = Modifier
                    .size(SETTINGS_BUTTON_SIZE)
                    .clickable { onSettingsClicked() }
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = SETTINGS_BUTTON_SIZE + SETTINGS_BUTTON_PADDING_TOP)
        ) {
            Switch(
                checked = screenState.switchChecked,
                onCheckedChange = { checked -> onSwitchCheckedChanged(checked) },
                modifier = Modifier
                    .scale(5f)
                    .rotate(270f)
            )
        }
    }
}

@Composable
private fun HomeScreenItem(
    itemSize: Size,
    itemTitle: String,
    isCentralItem: Boolean,
) {
    Card(
        shape = RoundedCornerShape(0.dp),
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .width(itemSize.width.dp)
            .height(itemSize.height.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = itemTitle,
                fontSize = if (isCentralItem) 24.sp else 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        screenState = HomeScreenState(),
        onModeSelected = {},
        onSettingsClicked = {},
        onSwitchCheckedChanged = {},
    )
}

private class CircularAdapter(
    private val content: List<Int>,
    listSize: Int,
) : List<Int> {
    override val size: Int = listSize
    override fun contains(element: Int): Boolean = content.contains(element = element)
    override fun containsAll(elements: Collection<Int>): Boolean = content.containsAll(elements)
    override fun get(index: Int): Int = content[index % content.size]
    override fun indexOf(element: Int): Int = content.indexOf(element)
    override fun isEmpty(): Boolean = content.isEmpty()
    override fun iterator(): Iterator<Int> = content.iterator()
    override fun lastIndexOf(element: Int): Int = content.lastIndexOf(element)
    override fun listIterator(): ListIterator<Int> = content.listIterator()
    override fun listIterator(index: Int): ListIterator<Int> = content.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<Int> =
        content.subList(fromIndex, toIndex)
}
