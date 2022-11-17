package com.josycom.mayorjay.genpass.view.onboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.josycom.mayorjay.genpass.data.models.OnboardData

class OnboardPagerAdapter(fragmentActivity: FragmentActivity, private val onboardData: List<OnboardData>) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return onboardData.size
    }

    override fun createFragment(position: Int): Fragment {
        return OnboardFragment.newInstance(onboardData[position])
    }
}