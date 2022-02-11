package com.josycom.mayorjay.genpass.home.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.genpass.databinding.FragmentGeneratePasswordBinding

class GeneratePasswordFragment : Fragment() {

    private lateinit var viewModel: GeneratePasswordViewModel
    private lateinit var binding: FragmentGeneratePasswordBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeneratePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GeneratePasswordViewModel::class.java]
    }
}