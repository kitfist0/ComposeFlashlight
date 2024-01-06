package app.flashlight.ui.screen.settings

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.flashlight.R
import de.palm.composestateevents.EventEffect
import de.palm.composestateevents.StateEventWithContentTriggered
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    val screenState: SettingsScreenState by viewModel.screenState.collectAsState()

    SettingsScreenContent(
        screenState = screenState,
        onItemClicked = { item -> viewModel.onSettingItemClicked(item) },
    )

    val context = LocalContext.current
    (screenState.bottomSheetEvent as? StateEventWithContentTriggered<LongArray>)?.let { event ->
        SettingsScreenModalBottomSheet(
            onDismiss = { viewModel.onConsumedBottomSheetEvent() },
            items = event.content.toList(),
            itemText = stringResource(R.string.settings_shutdown_timeout_in_minutes),
        )
    }
    EventEffect(
        event = screenState.viewIntentEvent,
        onConsumed = viewModel::onConsumedViewIntentEvent,
    ) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
    }
    EventEffect(
        event = screenState.longToastEvent,
        onConsumed = viewModel::onConsumedToastEvent,
    ) {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
    }
}

@Composable
private fun SettingsScreenContent(
    screenState: SettingsScreenState,
    onItemClicked: (SettingItemId) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(screenState.titleStringRes),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = screenState.versionNameText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
        item {
            SettingsScreenItem(
                settingItemState = screenState.themeSettingItem,
                onClick = { onItemClicked.invoke(SettingItemId.THEME) },
            )
        }
        item {
            SettingsScreenItem(
                settingItemState = screenState.shutdownTimeoutItem,
                onClick = { onItemClicked.invoke(SettingItemId.TIMEOUT) },
            )
        }
        item {
            SettingsScreenItem(
                settingItemState = screenState.githubSettingItem,
                onClick = { onItemClicked.invoke(SettingItemId.GITHUB) },
            )
        }
        item {
            SettingsScreenItem(
                settingItemState = screenState.policySettingItem,
                onClick = { onItemClicked.invoke(SettingItemId.POLICY) },
            )
        }
    }
}

@Composable
private fun SettingsScreenItem(
    settingItemState: SettingItemState,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .clickable { onClick.invoke() }
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(60.dp)
        ) {
            Image(
                painter = painterResource(settingItemState.iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(settingItemState.titleRes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SettingsScreenModalBottomSheet(
    onDismiss: () -> Unit,
    items: List<T>,
    itemText: String,
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = { onDismiss.invoke() },
        sheetState = sheetState,
    ) {
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            items.forEach {
                Text(
                    text = itemText.format(it),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss.invoke()
                                }
                            }
                        }
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreenContent(
        screenState = SettingsScreenState(),
        onItemClicked = {},
    )
}
