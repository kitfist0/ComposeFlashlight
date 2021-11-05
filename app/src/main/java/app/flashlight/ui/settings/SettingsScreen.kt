package app.flashlight.ui.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.flashlight.R

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {

    val state = viewModel.settingsScreenState.collectAsState().value

    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(state.titleStringRes),
                    fontSize = 22.sp,
                    color = MaterialTheme.colors.onBackground,
                )
                Text(
                    text = state.versionNameText,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }

        item {
            SettingsScreenItem(
                titleRes = R.string.theme,
                iconRes = R.drawable.ic_twotone_palette,
                onClick = { viewModel.onThemeItemClicked() },
            )
        }

        item {
            SettingsScreenItem(
                titleRes = R.string.github,
                iconRes = R.drawable.ic_twotone_github,
                onClick = { viewModel.onGitHubItemClicked() },
            )
        }

        item {
            SettingsScreenItem(
                titleRes = R.string.copyright,
                iconRes = R.drawable.ic_twotone_copyright,
                onClick = { viewModel.onLicenseItemClicked() },
            )
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
            .background(MaterialTheme.colors.background)
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
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(titleRes),
                color = MaterialTheme.colors.onSurface,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}
