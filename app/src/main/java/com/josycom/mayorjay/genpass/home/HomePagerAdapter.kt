package com.josycom.mayorjay.genpass.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.josycom.mayorjay.genpass.home.generate.GeneratePasswordFragment
import com.josycom.mayorjay.genpass.home.view.PasswordListFragment

class HomePagerAdapter(private val fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        if (position == 0) return GeneratePasswordFragment()
        return PasswordListFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(
        holder: FragmentViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val tag = "f${holder.itemId}"
        val fragment: Fragment? = fragmentActivity.supportFragmentManager.findFragmentByTag(tag)
        if (fragment != null && fragment is PasswordListFragment) {
            fragment.displayPasswords()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}