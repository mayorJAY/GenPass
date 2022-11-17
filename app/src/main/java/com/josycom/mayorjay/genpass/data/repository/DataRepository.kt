package com.josycom.mayorjay.genpass.data.repository

import kotlinx.coroutines.flow.Flow

interface DataRepository {

    fun getBooleanPreferenceFlow(key: String): Flow<Boolean?>
    suspend fun setBooleanPreference(key: String, value: Boolean)
    fun getStringPreferenceFlow(key: String): Flow<String?>
    suspend fun setStringPreference(key: String, value: String)
    fun getIntPreferenceFlow(key: String): Flow<Int?>
    suspend fun setIntPreference(key: String, value: Int)
    suspend fun deleteAllPreferences()
}