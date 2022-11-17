package com.josycom.mayorjay.genpass.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.data.repository.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {

    var appOpenCountLiveData: LiveData<Int?>? = null

    fun getAppOpenCountPref(key: String): Flow<Int?> {
        return dataRepository.getIntPreferenceFlow(key).apply {
            appOpenCountLiveData = this.asLiveData()
        }
    }

    fun saveAppOpenCounts(key: String, value: Int) {
        viewModelScope.launch {
            dataRepository.setIntPreference(key, value)
        }
    }
}

class HomeViewModelFactory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(dataRepository) as T
    }
}