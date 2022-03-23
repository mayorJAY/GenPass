package com.josycom.mayorjay.genpass.home.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josycom.mayorjay.genpass.data.PasswordData

class PasswordListViewModel : ViewModel() {
    val passwordList = MutableLiveData<List<PasswordData>>()
}