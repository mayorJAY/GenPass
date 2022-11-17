package com.josycom.mayorjay.genpass.testdata

import com.josycom.mayorjay.genpass.data.preferencedatastore.PreferenceDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePreferenceDataSource : PreferenceDataSource {

    private val fakeDataStore: MutableMap<String, Any> = mutableMapOf()

    override fun getBooleanPreferenceFlow(key: String): Flow<Boolean?> {
        return flow {
            emit(fakeDataStore[key] as Boolean?)
        }
    }

    override suspend fun setBooleanPreference(key: String, value: Boolean) {
        fakeDataStore[key] = value
    }

    override fun getStringPreferenceFlow(key: String): Flow<String?> {
        return flow {
            emit(fakeDataStore[key] as String?)
        }
    }

    override suspend fun setStringPreference(key: String, value: String) {
        fakeDataStore[key] = value
    }

    override fun getIntPreferenceFlow(key: String): Flow<Int?> {
        return flow {
            emit(fakeDataStore[key] as Int?)
        }
    }

    override suspend fun setIntPreference(key: String, value: Int) {
        fakeDataStore[key] = value
    }

    override suspend fun deleteAllPreferences() {
        fakeDataStore.clear()
    }

    fun isEmpty() = fakeDataStore.isEmpty()
}