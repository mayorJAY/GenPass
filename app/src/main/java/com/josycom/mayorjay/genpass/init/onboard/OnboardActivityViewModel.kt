package com.josycom.mayorjay.genpass.init.onboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import kotlinx.coroutines.launch

class OnboardActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var preferenceManager: PreferenceManager? = null

    init {
        preferenceManager = PreferenceManager(application.dataStore)
    }

    fun setPref() {
        viewModelScope.launch {
            preferenceManager?.setFirstLaunchPref(false)
        }
    }
}