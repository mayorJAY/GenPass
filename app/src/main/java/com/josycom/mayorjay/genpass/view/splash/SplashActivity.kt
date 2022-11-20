package com.josycom.mayorjay.genpass.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.josycom.mayorjay.genpass.R
import com.josycom.mayorjay.genpass.viewmodel.SplashViewModel
import com.josycom.mayorjay.genpass.viewmodel.SplashViewModelFactory
import com.josycom.mayorjay.genpass.view.home.HomeActivity
import com.josycom.mayorjay.genpass.view.onboard.OnboardActivity
import com.josycom.mayorjay.genpass.data.preferencedatastore.PreferenceDataSourceImpl
import com.josycom.mayorjay.genpass.data.preferencedatastore.dataStore
import com.josycom.mayorjay.genpass.data.repository.DataRepositoryImpl
import com.josycom.mayorjay.genpass.util.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var isFirstLaunch = true
    private var job: Job? = null
    private val viewModel: SplashViewModel by viewModels {
        val dataSource = PreferenceDataSourceImpl(dataStore)
        val repository = DataRepositoryImpl(dataSource)
        SplashViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        viewModel.deletePreferences()
        splashScreen.setKeepOnScreenCondition { true }
        getLaunchPref()
        observeLaunchPref()
        startJob()
    }

    private fun getLaunchPref() {
        viewModel.getLaunchPref(Constants.FIRST_LAUNCH_PREF_KEY)
    }

    private fun observeLaunchPref() {
        viewModel.isFirstLaunch?.observe(this) { value ->
            isFirstLaunch = value ?: true
        }
    }

    private fun startJob() {
        job = lifecycleScope.launch {
            delay(1000)
            proceedToNextView()
        }
    }

    private fun proceedToNextView() {
        val clazz = if (isFirstLaunch) OnboardActivity::class.java else HomeActivity::class.java
        Intent(this, clazz).apply { startActivity(this) }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}