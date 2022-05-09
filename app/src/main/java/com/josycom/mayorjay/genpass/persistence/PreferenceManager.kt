package com.josycom.mayorjay.genpass.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.josycom.mayorjay.genpass.util.Constants.PREFERENCES_FILE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.apache.commons.lang3.StringUtils

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_FILE_NAME
)

class PreferenceManager(private val dataStore: DataStore<Preferences>) : IPreferenceManager {

    override fun getBooleanPreferenceFlow(key: String): Flow<Boolean> {
        val prefKey: Preferences.Key<Boolean> = booleanPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[prefKey] ?: true
            }
    }

    override suspend fun setBooleanPreference(
        key: String,
        value: Boolean
    ) {
        val prefKey: Preferences.Key<Boolean> = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    override fun getStringPreferenceFlow(key: String): Flow<String> {
        val prefKey: Preferences.Key<String> = stringPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[prefKey] ?: StringUtils.EMPTY
            }
    }

    override suspend fun setStringPreference(
        key: String,
        value: String
    ) {
        val prefKey: Preferences.Key<String> = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    override fun getIntPreferenceFlow(key: String): Flow<Int> {
        val prefKey: Preferences.Key<Int> = intPreferencesKey(key)
        return dataStore.data
            .catch { exception ->
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[prefKey] ?: 0
            }
    }

    override suspend fun setIntPreference(
        key: String,
        value: Int
    ) {
        val prefKey: Preferences.Key<Int> = intPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    override suspend fun deleteAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
