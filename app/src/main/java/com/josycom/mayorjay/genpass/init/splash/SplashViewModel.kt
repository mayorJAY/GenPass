package com.josycom.mayorjay.genpass.init.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.persistence.IPreferenceManager
import kotlinx.coroutines.launch

class SplashViewModel(private val preferenceManager: IPreferenceManager) : ViewModel() {

    var isFirstLaunch: LiveData<Boolean>? = null

    fun getLaunchPref(key: String) {
        isFirstLaunch = preferenceManager.getBooleanPreferenceFlow(key).asLiveData()
    }

    fun deletePreferences() {
        viewModelScope.launch {
            preferenceManager.deleteAllPreferences()
        }
    }
}

class SplashViewModelFactory(private val preferenceManager: IPreferenceManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(preferenceManager) as T
    }
}