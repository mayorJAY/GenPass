package com.josycom.mayorjay.genpass.data.repository

import com.josycom.mayorjay.genpass.data.preferencedatastore.PreferenceDataSource
import kotlinx.coroutines.flow.Flow

class DataRepositoryImpl(private val preferenceDataSource: PreferenceDataSource) : DataRepository {

    override fun getBooleanPreferenceFlow(key: String): Flow<Boolean?> {
        return preferenceDataSource.getBooleanPreferenceFlow(key)
    }

    override suspend fun setBooleanPreference(key: String, value: Boolean) {
        preferenceDataSource.setBooleanPreference(key, value)
    }

    override fun getStringPreferenceFlow(key: String): Flow<String?> {
        return preferenceDataSource.getStringPreferenceFlow(key)
    }

    override suspend fun setStringPreference(key: String, value: String) {
        preferenceDataSource.setStringPreference(key, value)
    }

    override fun getIntPreferenceFlow(key: String): Flow<Int?> {
        return preferenceDataSource.getIntPreferenceFlow(key)
    }

    override suspend fun setIntPreference(key: String, value: Int) {
        preferenceDataSource.setIntPreference(key, value)
    }

    override suspend fun deleteAllPreferences() {
        preferenceDataSource.deleteAllPreferences()
    }
}