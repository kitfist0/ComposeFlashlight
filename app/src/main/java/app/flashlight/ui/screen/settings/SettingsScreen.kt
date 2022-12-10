package app.flashlight.ui.screen.settings

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.palm.composestateevents.EventEffect

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    val screenState: SettingsScreenState by viewModel.screenState.collectAsState()

    SettingsScreenContent(
        screenState = screenState,
        onItemClicked = { item -> viewModel.onSettingsItemClicked(item) },
    )

    val context = LocalContext.current
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
    onItemClicked: (SettingsItem) -> Unit,
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

        for (settingsItem in SettingsItem.values()) {
            item {
                SettingsScreenItem(
                    titleRes = settingsItem.titleRes,
                    iconRes = settingsItem.iconRes,
                    onClick = { onItemClicked(settingsItem) },
                )
            }
        }
    }
}

@Composable
private fun SettingsScreenItem(
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int,
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
                painter = painterResource(iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
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
