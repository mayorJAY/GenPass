package com.josycom.mayorjay.genpass.home

import androidx.lifecycle.asLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.persistence.IPreferenceManager
import kotlinx.coroutines.launch

class HomeViewModel(private val preferenceManager: IPreferenceManager) : ViewModel() {

    var appOpenCountLiveData: LiveData<Int>? = null

    fun getAppOpenCounts(key: String) {
        appOpenCountLiveData = preferenceManager.getIntPreferenceFlow(key).asLiveData()
    }

    fun saveAppOpenCounts(key: String, value: Int) {
        viewModelScope.launch {
            preferenceManager.setIntPreference(key, value)
        }
    }
}

class HomeViewModelFactory(private val preferenceManager: IPreferenceManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(preferenceManager) as T
    }
}