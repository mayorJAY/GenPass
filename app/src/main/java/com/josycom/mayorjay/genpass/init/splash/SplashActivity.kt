package com.josycom.mayorjay.genpass.init.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.josycom.mayorjay.genpass.R
import com.josycom.mayorjay.genpass.home.HomeActivity
import com.josycom.mayorjay.genpass.init.onboard.OnboardActivity
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import com.josycom.mayorjay.genpass.util.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private var isFirstLaunch = true
    private var job: Job? = null
    private val viewModel: SplashViewModel by viewModels {
        val preferenceManager = PreferenceManager(dataStore)
        SplashViewModelFactory(preferenceManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        viewModel.deletePreferences()
        getLaunchPref()
        observeLaunchPref()
        startJob()
    }

    private fun getLaunchPref() {
        viewModel.getLaunchPref(Constants.FIRST_LAUNCH_PREF_KEY)
    }

    private fun observeLaunchPref() {
        viewModel.isFirstLaunch?.observe(this, { value ->
            isFirstLaunch = value ?: true
        })
    }

    private fun startJob() {
        job = lifecycleScope.launch {
            delay(1500)
            proceedToNextView()
        }
    }

    private fun proceedToNextView() {
        Intent(this, if (isFirstLaunch) OnboardActivity::class.java else HomeActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}