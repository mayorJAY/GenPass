package com.josycom.mayorjay.genpass.home.generate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.genpass.databinding.FragmentGeneratePasswordBinding
import com.josycom.mayorjay.genpass.util.Utilities
import org.apache.commons.lang3.StringUtils

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

        setupSpinnerAdapter()
        setupListeners()
        observePassword()
    }

    private fun setupSpinnerAdapter() {
        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.getPasswordTypes()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spPasswordType.adapter = this
        }
    }

    private fun observePassword() {
        viewModel.password.observe(viewLifecycleOwner, { value ->
            binding.tvPassword.text = value
            binding.ivCopy.isVisible = StringUtils.isNotBlank(value)
            binding.ivShare.isVisible = StringUtils.isNotBlank(value)
        })
    }

    private fun setupListeners() {
        binding.spPasswordType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.passwordType.value = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }

        binding.etPasswordLength.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.passwordLength.value = s?.toString()
            }
        })

        binding.btGenerate.setOnClickListener {
            val message = viewModel.validateInputs()
            if (StringUtils.isBlank(message)) {
                viewModel.password.value = ""
                val password = viewModel.generatePassword(viewModel.passwordType.value!!, viewModel.passwordLength.value?.toInt() ?: 0)
                viewModel.password.value = password
            } else {
                Utilities.showToast(message, requireContext())
            }
        }

        binding.ivCopy.setOnClickListener {
            Utilities.copyContentToClipboard(viewModel.password.value!!, requireContext())
            Utilities.showToast("Password copied", requireContext())
        }

        binding.ivShare.setOnClickListener {
            Utilities.shareContent(viewModel.password.value!!, requireContext())
        }
    }
}