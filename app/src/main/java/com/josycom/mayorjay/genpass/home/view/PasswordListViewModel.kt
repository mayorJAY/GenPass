package com.josycom.mayorjay.genpass.home.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.josycom.mayorjay.genpass.data.PasswordData
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore

class PasswordListViewModel(application: Application) : AndroidViewModel(application) {

    var preferenceManager: PreferenceManager? = null
    val tempPasswordList = mutableListOf<PasswordData>()
    val passwordList = MutableLiveData<List<PasswordData>>()

    init {
        preferenceManager = PreferenceManager(application.dataStore)
    }
}