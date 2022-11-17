package com.josycom.mayorjay.genpass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.josycom.mayorjay.genpass.data.repository.DataRepository
import kotlinx.coroutines.launch

class OnboardActivityViewModel(private val dataRepository: DataRepository) : ViewModel() {

    fun setLaunchPref(key: String, value: Boolean) {
        viewModelScope.launch {
            dataRepository.setBooleanPreference(key, value)
        }
    }
}

class OnboardActivityViewModelFactory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnboardActivityViewModel(dataRepository) as T
    }
}