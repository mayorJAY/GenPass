package com.josycom.mayorjay.genpass.init.onboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.persistence.IPreferenceManager
import kotlinx.coroutines.launch

class OnboardActivityViewModel(private val preferenceManager: IPreferenceManager) : ViewModel() {

    fun setLaunchPref(key: String, value: Boolean) {
        viewModelScope.launch {
            preferenceManager.setBooleanPreference(key, value)
        }
    }
}

class OnboardActivityViewModelFactory(private val preferenceManager: IPreferenceManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnboardActivityViewModel(preferenceManager) as T
    }
}