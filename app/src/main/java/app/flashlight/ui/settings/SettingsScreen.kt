package app.flashlight.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.flashlight.R

@Composable
fun SettingsScreen() {

    LazyColumn {

        item {
            Text(
                text = "Settings",
                fontSize = 32.sp,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }

        item {
            SettingsScreenItem(
                title = "GitHub page"
            )
        }

        item {
            SettingsScreenItem(
                title = "License page"
            )
        }
    }
}

@Composable
private fun SettingsScreenItem(
    title: String,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .height(70.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = title,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(name = "Settings Screen", showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}