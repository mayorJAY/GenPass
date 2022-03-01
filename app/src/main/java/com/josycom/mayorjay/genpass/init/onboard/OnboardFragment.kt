package com.josycom.mayorjay.genpass.init.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.josycom.mayorjay.genpass.data.OnboardData
import com.josycom.mayorjay.genpass.databinding.FragmentOnboardBinding

class OnboardFragment : Fragment() {

    private val viewModel: OnboardViewModel by viewModels()
    private lateinit var binding: FragmentOnboardBinding
    private var onboardData: OnboardData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnboardData()
        observeOnboardData()
    }

    private fun observeOnboardData() {
        viewModel.onboardData.observe(viewLifecycleOwner, { data ->
            binding.apply {
                ivOnboard.setImageDrawable(resources.getDrawable(data.image, null))
                tvHeading.text = data.header
                tvInfo.text = data.info
            }
        })
    }

    private fun setOnboardData() {
        if (onboardData != null) {
            viewModel.setOnboardData(onboardData!!)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(onboardData: OnboardData): OnboardFragment {
            return OnboardFragment().apply {
                this.onboardData = onboardData
            }
        }
    }
}