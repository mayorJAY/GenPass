package com.josycom.mayorjay.genpass.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.josycom.mayorjay.genpass.util.Constants.PREFERENCES_FILE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_FILE_NAME
)

class PreferenceManager(private val dataStore: DataStore<Preferences>) {

    private val isFirstLaunchPref = booleanPreferencesKey("isFirstLaunch")

    private fun getBooleanPreferenceFlow(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[key] ?: true
            }
    }

    private suspend fun setBooleanPreference(
        key: Preferences.Key<Boolean>,
        value: Boolean
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    private fun getStringPreferenceFlow(key: Preferences.Key<String>): Flow<String>  {
        return dataStore.data
            .catch { exception ->
                exception.printStackTrace()
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[key] ?: ""
            }
    }

    private suspend fun setStringPreference(
        key: Preferences.Key<String>,
        value: String
    ) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun deleteAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun getFirstLaunchPrefFlow(): Flow<Boolean> {
        return getBooleanPreferenceFlow(isFirstLaunchPref)
    }

    suspend fun setFirstLaunchPref(value: Boolean) {
        setBooleanPreference(isFirstLaunchPref, value)
    }

    fun getPasswordPrefFlow(key: String): Flow<String> {
        val passwordPref = stringPreferencesKey(key)
        return getStringPreferenceFlow(passwordPref)
    }

    suspend fun setPasswordPref(key: String, value: String) {
        val passwordPref = stringPreferencesKey(key)
        setStringPreference(passwordPref, value)
    }
}
