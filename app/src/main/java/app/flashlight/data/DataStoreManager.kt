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

    suspend fun setIsEnabled(b: Boolean) = dataStore.edit { preferences ->
        preferences[IS_ENABLED_KEY] = b
    }

    val isEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_ENABLED_KEY] ?: false
    }

    suspend fun setMode(mode: Int) = dataStore.edit { preferences ->
        preferences[MODE_KEY] = mode
    }

    val mode: Flow<Int> = dataStore.data.map { preferences ->
        preferences[MODE_KEY] ?: DataConstants.DEFAULT_MODE_ID
    }

    companion object {
        private val MODE_KEY = intPreferencesKey("mode")
        private val IS_ENABLED_KEY = booleanPreferencesKey("is_enabled")
    }
}