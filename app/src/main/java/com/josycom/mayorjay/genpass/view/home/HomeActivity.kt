package com.josycom.mayorjay.genpass.view.home

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
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
import com.josycom.mayorjay.genpass.viewmodel.HomeViewModel
import com.josycom.mayorjay.genpass.viewmodel.HomeViewModelFactory
import com.josycom.mayorjay.genpass.data.preferencedatastore.PreferenceDataSourceImpl
import com.josycom.mayorjay.genpass.data.preferencedatastore.dataStore
import com.josycom.mayorjay.genpass.data.repository.DataRepositoryImpl
import com.josycom.mayorjay.genpass.util.Constants
import com.josycom.mayorjay.genpass.util.Constants.APP_UPDATE
import com.josycom.mayorjay.genpass.util.showSnackBar
import com.josycom.mayorjay.genpass.util.showToast
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeActivity : AppCompatActivity() {

    private var appOpenCount = 0
    private var job: Job? = null
    private lateinit var binding: ActivityHomeBinding
    private var appUpdateManager: AppUpdateManager? = null
    private val viewModel: HomeViewModel by viewModels {
        val dataSource = PreferenceDataSourceImpl(dataStore)
        val repository = DataRepositoryImpl(dataSource)
        HomeViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        getAppOpenCounts()
        observeAppOpenCount()
        startJob()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = HomePagerAdapter(this)
        val tabTitles = arrayOf(R.string.tab_text_1, R.string.tab_text_2)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(tabTitles[position])
        }.attach()
    }

    private fun getAppOpenCounts() {
        viewModel.getAppOpenCountPref(Constants.APP_OPEN_COUNT_PREF_KEY)
    }

    private fun observeAppOpenCount() {
        viewModel.appOpenCountLiveData?.observe(this) { value ->
            appOpenCount = value ?: 0
        }
    }

    private fun startJob() {
        job = lifecycleScope.launch {
            delay(500)
            displayAppReviewPrompt()
        }
    }

    private fun displayAppReviewPrompt() {
        val currentAppOpenCount = appOpenCount.plus(1)
        viewModel.saveAppOpenCounts(Constants.APP_OPEN_COUNT_PREF_KEY, currentAppOpenCount)
        if (currentAppOpenCount == 5 || currentAppOpenCount == 10) {
            AlertDialog.Builder(this).apply {
                setCancelable(false)
                setMessage(getString(R.string.app_review_msg))
                setNegativeButton(getString(R.string.i_am_good)) { _: DialogInterface, _: Int -> }
                setPositiveButton(getString(R.string.i_will_rate)) { _: DialogInterface, _: Int ->
                    val uri = Uri.parse("market://details?id=$packageName")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    if (packageManager.queryIntentActivities(intent, 0).size <= 0) {
                        showToast(getString(R.string.play_store_error))
                        return@setPositiveButton
                    }
                    startActivity(intent)
                }
                show()
            }
        }
    }

    private val callback = object : ViewPager2.OnPageChangeCallback() {
        @SuppressLint("NotifyDataSetChanged")
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

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
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
                    Timber.i("UpdateInstaller InstallStateUpdatedListener >>>>> ${state.installStatus()}")
                }
            }
        }
    }

    private val onSuccessListener = OnSuccessListener<AppUpdateInfo> { appUpdateInfo ->
        if (appUpdateInfo?.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
            try {
                appUpdateManager?.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, APP_UPDATE)
            } catch (e: IntentSender.SendIntentException) {
                Timber.e(e)
            }
        } else if (appUpdateInfo?.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackBarForDownloadedUpdate()
        } else {
            Timber.i("UpdateInstaller OnSuccessListener >>>>> ${appUpdateInfo?.installStatus()}")
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