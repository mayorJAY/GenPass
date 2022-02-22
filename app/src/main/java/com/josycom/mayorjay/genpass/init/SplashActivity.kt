package com.josycom.mayorjay.genpass.init

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.josycom.mayorjay.genpass.R
import com.josycom.mayorjay.genpass.home.HomeActivity
import com.josycom.mayorjay.genpass.init.onboard.OnboardActivity
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private var isFirstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val dataStore = PreferenceManager(dataStore)
//        lifecycleScope.launch {
//            dataStore.deleteAllPreferences()
//        }
        dataStore.getFirstLaunchPrefFlow().asLiveData().observe(this, { value ->
            isFirstLaunch = value
        })
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ proceedToNextView() }, 1000)
    }

    private fun proceedToNextView() {
        val intent = Intent(this, if (isFirstLaunch) OnboardActivity::class.java else HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}