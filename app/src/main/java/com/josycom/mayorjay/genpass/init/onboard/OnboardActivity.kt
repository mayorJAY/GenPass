package com.josycom.mayorjay.genpass.init.onboard

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.josycom.mayorjay.genpass.R
import com.josycom.mayorjay.genpass.data.OnboardData
import com.josycom.mayorjay.genpass.databinding.ActivityOnboardBinding
import com.josycom.mayorjay.genpass.home.HomeActivity
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import com.josycom.mayorjay.genpass.util.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class OnboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardBinding
    private val viewModel: OnboardActivityViewModel by viewModels {
        val preferenceManager = PreferenceManager(dataStore)
        OnboardActivityViewModelFactory(preferenceManager)
    }
    private val dots: ArrayList<ImageView> = ArrayList()
    private var currentPage = 0
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupListeners()
        enableViewPagerAutoScroll()
        setLaunchPref()
    }

    private fun setupViews() {
        val onboardPagerAdapter = OnboardPagerAdapter(this, getOnboardData()).apply {
            binding.viewPager.adapter = this
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(12, 0, 12, 0)
        }
        for (i in 0 until onboardPagerAdapter.itemCount) {
            dots.add(ImageView(this).apply {
                setImageDrawable(resources.getDrawable(R.drawable.non_selected_item_dot, null))
                binding.viewPagerCounterLayout.addView(this, params)
            })
        }
        dots[0].setImageDrawable(resources.getDrawable(R.drawable.selected_item_dot, null))
    }

    private fun setupListeners() {
        binding.btProceed.setOnClickListener {
            navigateToHome()
        }
    }

    private fun enableViewPagerAutoScroll() {
        job = lifecycleScope.launch {
            delay(500)
            while (true) {
                if (currentPage == dots.size) currentPage = 0
                binding.viewPager.setCurrentItem(currentPage++, true)
                delay(2000)
            }
        }
    }

    private fun setLaunchPref() {
        viewModel.setLaunchPref(Constants.FIRST_LAUNCH_PREF_KEY, false)
    }

    private fun navigateToHome() {
        Intent(this, HomeActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    private val callback = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentPage = position
            dots.forEach { view ->
                view.setImageDrawable(resources.getDrawable(R.drawable.non_selected_item_dot, null))
            }

            dots[position].setImageDrawable(
                resources.getDrawable(
                    R.drawable.selected_item_dot,
                    null
                )
            )
            binding.btProceed.isVisible = (position == dots.lastIndex)
        }
    }

    private fun getOnboardData() = mutableListOf<OnboardData>().apply {
            add(
                OnboardData(
                    R.drawable.ic_onboard_1,
                    getString(R.string.onboard_1_heading),
                    getString(R.string.onboard_1_info)
                )
            )
            add(
                OnboardData(
                    R.drawable.ic_onboard_2,
                    getString(R.string.onboard_2_heading),
                    getString(R.string.onboard_2_info)
                )
            )
            add(
                OnboardData(
                    R.drawable.ic_onboard_3,
                    getString(R.string.onboard_3_heading),
                    getString(R.string.onboard_3_info)
                )
            )
        }

    override fun onStart() {
        super.onStart()
        binding.viewPager.registerOnPageChangeCallback(callback)
    }

    override fun onStop() {
        super.onStop()
        binding.viewPager.unregisterOnPageChangeCallback(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}