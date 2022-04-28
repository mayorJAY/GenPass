package com.josycom.mayorjay.genpass.home.view

import androidx.lifecycle.asLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.genpass.data.PasswordData
import com.josycom.mayorjay.genpass.persistence.IPreferenceManager

class PasswordListViewModel(private val preferenceManager: IPreferenceManager) : ViewModel() {

    val tempPasswordList = mutableListOf<PasswordData>()
    val passwordList = MutableLiveData<List<PasswordData>>()

    fun getPasswordPref(key: String): LiveData<String> {
        return preferenceManager.getStringPreferenceFlow(key).asLiveData()
    }
}

class PasswordListViewModelFactory(private val preferenceManager: IPreferenceManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordListViewModel(preferenceManager) as T
    }
}