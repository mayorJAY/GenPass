package com.josycom.mayorjay.genpass.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.josycom.mayorjay.genpass.home.generate.GeneratePasswordFragment
import com.josycom.mayorjay.genpass.home.view.PasswordListFragment

class HomePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        if (position == 0) return GeneratePasswordFragment()
        return PasswordListFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}