package com.josycom.mayorjay.genpass.init.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.persistence.IPreferenceManager
import com.josycom.mayorjay.genpass.util.Constants
import kotlinx.coroutines.launch

class OnboardActivityViewModel(private val preferenceManager: IPreferenceManager) : ViewModel() {

    fun setPref() {
        viewModelScope.launch {
            preferenceManager.setBooleanPreference(Constants.FIRST_LAUNCH_PREF_KEY, false)
        }
    }
}

class OnboardActivityViewModelFactory(private val preferenceManager: IPreferenceManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnboardActivityViewModel(preferenceManager) as T
    }
}