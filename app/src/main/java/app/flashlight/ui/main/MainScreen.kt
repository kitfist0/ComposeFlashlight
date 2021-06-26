package app.flashlight.ui.main

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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.flashlight.R
import app.flashlight.ui.SharedViewModel
import app.flashlight.ui.main.MainScreenState.Companion.centralVisibleIndex
import app.flashlight.ui.main.MainScreenState.Companion.getFirstIndex
import app.flashlight.ui.main.MainScreenState.Companion.getItemSize
import app.flashlight.ui.main.MainScreenState.Companion.setOnScrollFinishedListener
import kotlinx.coroutines.launch

private val SETTINGS_BUTTON_PADDING_TOP = 48.dp
private val SETTINGS_BUTTON_SIZE = 48.dp

@Composable
fun MainScreen(
    viewModel: SharedViewModel,
    onSettingsClick: () -> Unit
) {

    val viewState by viewModel.mainScreenState.collectAsState()
    val items = CircularAdapter(viewState.modes.toList())
    val itemSize = LocalContext.current.resources.displayMetrics.getItemSize()
    val lazyListState = rememberLazyListState(viewState.getFirstIndex())
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyRow(state = lazyListState) {
            itemsIndexed(items) { ind, item ->
                Card(
                    shape = RoundedCornerShape(0.dp),
                    backgroundColor = MaterialTheme.colors.surface,
                    modifier = Modifier
                        .width(itemSize.dp)
                        .height(itemSize.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.toString(),
                            fontSize = if (ind == lazyListState.centralVisibleIndex()) 24.sp else 20.sp
                        )
                    }
                }
                lazyListState.setOnScrollFinishedListener { firstIndex, selectedMode ->
                    scope.launch {
                        lazyListState.animateScrollToItem(firstIndex)
                        viewModel.onModeSelected(selectedMode)
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
                    .clickable { onSettingsClick.invoke() },
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
                checked = viewState.switchChecked,
                onCheckedChange = { viewModel.onSwitchCheckedChanged(it) },
                modifier = Modifier
                    .scale(5f)
                    .rotate(270f)
            )
        }
    }
}

private class CircularAdapter(
    private val content: List<Int>
) : List<Int> {
    override val size: Int = Int.MAX_VALUE
    override fun contains(element: Int): Boolean = content.contains(element = element)
    override fun containsAll(elements: Collection<Int>): Boolean = content.containsAll(elements)
    override fun get(index: Int): Int = content[index % content.size]
    override fun indexOf(element: Int): Int = content.indexOf(element)
    override fun isEmpty(): Boolean = content.isEmpty()
    override fun iterator(): Iterator<Int> = content.iterator()
    override fun lastIndexOf(element: Int): Int = content.lastIndexOf(element)
    override fun listIterator(): ListIterator<Int> = content.listIterator()
    override fun listIterator(index: Int): ListIterator<Int> = content.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int): List<Int> = content.subList(fromIndex, toIndex)
}