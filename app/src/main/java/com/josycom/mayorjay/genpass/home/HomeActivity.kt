package com.josycom.mayorjay.genpass.home

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.OnSuccessListener
import com.josycom.mayorjay.genpass.R
import com.josycom.mayorjay.genpass.databinding.ActivityHomeBinding
import com.josycom.mayorjay.genpass.util.Constants.APP_UPDATE
import com.josycom.mayorjay.genpass.util.showSnackBar

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var appUpdateManager: AppUpdateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        //Todo: Implement app review prompt
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = HomePagerAdapter(this)
        val tabTitles = arrayOf(R.string.tab_text_1, R.string.tab_text_2)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(tabTitles[position])
        }.attach()
    }

    private val callback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.viewPager.adapter?.notifyDataSetChanged()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.viewPager.registerOnPageChangeCallback(callback)
    }

    override fun onResume() {
        super.onResume()
        checkForAppUpdate()
    }

    override fun onPause() {
        super.onPause()
        appUpdateManager?.unregisterListener(installStateUpdatedListener)
    }

    override fun onStop() {
        super.onStop()
        binding.viewPager.unregisterOnPageChangeCallback(callback)
    }

    private fun checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager?.registerListener(installStateUpdatedListener)
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener(onSuccessListener)
    }

    private val installStateUpdatedListener = object: InstallStateUpdatedListener {
        override fun onStateUpdate(state: InstallState) {
            when (state.installStatus()) {
                InstallStatus.DOWNLOADED -> {
                    popupSnackBarForDownloadedUpdate()
                }
                InstallStatus.INSTALLED -> {
                    appUpdateManager?.unregisterListener(this)
                }
                else -> {
                    Log.i("UpdateInstaller", "InstallStateUpdatedListener >>>>> ${state.installStatus()}")
                }
            }
        }
    }

    private val onSuccessListener = OnSuccessListener<AppUpdateInfo> { appUpdateInfo ->
        if (appUpdateInfo?.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
            try {
                appUpdateManager?.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, APP_UPDATE)
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }
        } else if (appUpdateInfo?.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackBarForDownloadedUpdate()
        } else {
            Log.i("UpdateInstaller", "OnSuccessListener >>>>> ${appUpdateInfo?.installStatus()}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE && resultCode != RESULT_OK) {
            checkForAppUpdate()
        }
    }

    private fun completeUpdate() {
        appUpdateManager?.completeUpdate()
    }

    private fun popupSnackBarForDownloadedUpdate() {
        binding.root.showSnackBar(
            getString(R.string.update_downloaded),
            Snackbar.LENGTH_INDEFINITE,
            getString(R.string.restart),
            ContextCompat.getColor(this@HomeActivity, R.color.colorWhite),
            this::completeUpdate
        )
    }
}