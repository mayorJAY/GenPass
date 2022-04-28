package com.josycom.mayorjay.genpass.home.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.genpass.data.PasswordData
import com.josycom.mayorjay.genpass.persistence.IPreferenceManager

class PasswordListViewModel(val preferenceManager: IPreferenceManager) : ViewModel() {

    val tempPasswordList = mutableListOf<PasswordData>()
    val passwordList = MutableLiveData<List<PasswordData>>()
}

class PasswordListViewModelFactory(private val preferenceManager: IPreferenceManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PasswordListViewModel(preferenceManager) as T
    }
}