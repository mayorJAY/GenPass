package com.josycom.mayorjay.genpass.testdata

import com.josycom.mayorjay.genpass.data.repository.DataRepository
import kotlinx.coroutines.flow.Flow

class FakeRepository(private val dataSource: FakePreferenceDataSource) : DataRepository {
    override fun getBooleanPreferenceFlow(key: String): Flow<Boolean?> {
        return dataSource.getBooleanPreferenceFlow(key)
    }

    override suspend fun setBooleanPreference(key: String, value: Boolean) {
        dataSource.setBooleanPreference(key, value)
    }

    override fun getStringPreferenceFlow(key: String): Flow<String?> {
        return dataSource.getStringPreferenceFlow(key)
    }

    override suspend fun setStringPreference(key: String, value: String) {
        dataSource.setStringPreference(key, value)
    }

    override fun getIntPreferenceFlow(key: String): Flow<Int?> {
        return dataSource.getIntPreferenceFlow(key)
    }

    override suspend fun setIntPreference(key: String, value: Int) {
        dataSource.setIntPreference(key, value)
    }

    override suspend fun deleteAllPreferences() {
        dataSource.deleteAllPreferences()
    }

    fun isEmpty() = dataSource.isEmpty()
}