package com.josycom.mayorjay.genpass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.data.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SplashViewModel(private val dataRepository: DataRepository) : ViewModel() {

    var isFirstLaunch: LiveData<Boolean?>? = null

    fun getLaunchPref(key: String): Flow<Boolean?> {
        return dataRepository.getBooleanPreferenceFlow(key).apply {
            isFirstLaunch = this.asLiveData()
        }
    }

    fun deletePreferences() {
        viewModelScope.launch {
            dataRepository.deleteAllPreferences()
        }
    }
}

class SplashViewModelFactory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(dataRepository) as T
    }
}