package com.josycom.mayorjay.genpass.init.onboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.josycom.mayorjay.genpass.data.OnboardData

class OnboardViewModel : ViewModel() {

    private val _onBoardData = MutableLiveData<OnboardData>()
    val onboardData: LiveData<OnboardData> = _onBoardData

    fun setOnboardData(onboardData: OnboardData) {
        _onBoardData.value = onboardData
    }
}