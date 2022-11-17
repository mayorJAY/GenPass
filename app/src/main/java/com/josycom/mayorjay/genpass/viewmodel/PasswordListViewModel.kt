package com.josycom.mayorjay.genpass.viewmodel

import androidx.lifecycle.asLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.genpass.data.models.PasswordData
import com.josycom.mayorjay.genpass.data.repository.DataRepository
import kotlinx.coroutines.flow.Flow

class PasswordListViewModel(private val dataRepository: DataRepository) : ViewModel() {

    val tempPasswordList = mutableListOf<PasswordData>()
    val passwordList = MutableLiveData<List<PasswordData>>()

    fun getPasswordPref(key: String): LiveData<String?> {
        return getPasswordPrefFlow(key).asLiveData()
    }

    fun getPasswordPrefFlow(key: String): Flow<String?> {
        return dataRepository.getStringPreferenceFlow(key)
    }
}

class PasswordListViewModelFactory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordListViewModel(dataRepository) as T
    }
}