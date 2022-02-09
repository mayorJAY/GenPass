package com.josycom.mayorjay.genpass.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        if (position == 0) return HomeFragment()
        return ListFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}