package app.flashlight.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("prefs")

@Singleton
class DataStoreManager @Inject constructor(appContext: Context) {

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
        Mode.entries[preferences[MODE_KEY] ?: Mode.DEFAULT_MODE.ordinal]
    }

    suspend fun setShutdownTimeout(timeout: Long) = dataStore.edit { preferences ->
        preferences[SHUTDOWN_TIMEOUT_KEY] = timeout
    }

    val shutdownTimeout: Flow<Long> = dataStore.data.map { preferences ->
        preferences[SHUTDOWN_TIMEOUT_KEY] ?: Long.MAX_VALUE
    }

    suspend fun setDarkThemeEnabled(b: Boolean) = dataStore.edit { preferences ->
        preferences[DARK_THEME_KEY] = b
    }

    val darkThemeEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: true
    }

    private companion object {
        val MODE_KEY = intPreferencesKey("mode")
        val FLASHLIGHT_ENABLED_KEY = booleanPreferencesKey("enabled")
        val SHUTDOWN_TIMEOUT_KEY = longPreferencesKey("shutdown_timeout")
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }
}
