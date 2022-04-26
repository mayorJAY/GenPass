package com.josycom.mayorjay.genpass.init.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import kotlinx.coroutines.launch

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    var preferenceManager: PreferenceManager? = null
    var isFirstLaunch: LiveData<Boolean>? = null

    init {
        preferenceManager = PreferenceManager(application.applicationContext.dataStore)
        isFirstLaunch = preferenceManager?.getFirstLaunchPrefFlow()?.asLiveData()
    }

    fun deletePreferences() {
        viewModelScope.launch {
            preferenceManager?.deleteAllPreferences()
        }
    }
}