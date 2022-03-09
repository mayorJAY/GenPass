package com.josycom.mayorjay.genpass.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.josycom.mayorjay.genpass.R
import com.josycom.mayorjay.genpass.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = HomePagerAdapter(this)
        val tabTitles = arrayOf(R.string.tab_text_1, R.string.tab_text_2)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getString(tabTitles[position])
        }.attach()
    }
}