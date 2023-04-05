package app.flashlight.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("prefs")

class DataStoreManager(appContext: Context) {

    private val dataStore = appContext.dataStore

    suspend fun setFlashlightEnabled(b: Boolean) = dataStore.edit { preferences ->
        preferences[FLASHLIGHT_ENABLED_KEY] = b
    }

    val flashlightEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[FLASHLIGHT_ENABLED_KEY] ?: false
    }

    suspend fun setMode(mode: Mode) = dataStore.edit { preferences ->
        preferences[MODE_KEY] = mode.ordinal
    }

    val mode: Flow<Mode> = dataStore.data.map { preferences ->
        Mode.values()[preferences[MODE_KEY] ?: Mode.DEFAULT_MODE.ordinal ]
    }

    suspend fun setDarkThemeEnabled(b: Boolean) = dataStore.edit { preferences ->
        preferences[DARK_THEME_KEY] = b
    }

    val darkThemeEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false
    }

    private companion object {
        val MODE_KEY = intPreferencesKey("mode")
        val FLASHLIGHT_ENABLED_KEY = booleanPreferencesKey("enabled")
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }
}
