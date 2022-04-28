package com.josycom.mayorjay.genpass.home.generate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.josycom.mayorjay.genpass.R
import com.josycom.mayorjay.genpass.data.PasswordData
import com.josycom.mayorjay.genpass.databinding.FragmentGeneratePasswordBinding
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import com.josycom.mayorjay.genpass.util.Constants
import com.josycom.mayorjay.genpass.util.copyContentToClipboard
import com.josycom.mayorjay.genpass.util.shareContent
import com.josycom.mayorjay.genpass.util.showToast
import org.apache.commons.lang3.StringUtils
import java.util.Date

class GeneratePasswordFragment : Fragment() {

    private val viewModel: GeneratePasswordViewModel by viewModels {
        val preferenceManager = PreferenceManager(requireContext().dataStore)
        GeneratePasswordViewModelFactory(preferenceManager)
    }
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

        observePasswordPref()
        setupSpinnerAdapter()
        setupListeners()
        observeGeneratedPassword()
    }

    private fun observePasswordPref() {
        for (key in Constants.PASSWORD_KEY_LIST) {
            viewModel.preferenceManager.getStringPreferenceFlow(key).asLiveData().observe(
                viewLifecycleOwner,
                { value ->
                    viewModel.queueToList = viewModel.passwordQueue.toList()
                    val passList = mutableListOf<String>()
                    for (item in viewModel.queueToList) {
                        passList.add("${item.password}-${item.timeGenerated}")
                    }
                    if (StringUtils.isNotBlank(value) && !passList.contains(value)) {
                        viewModel.passwordQueue.add(
                            PasswordData(
                                key, value.substringBefore("-"), value.substringAfter(
                                    "-"
                                ).toLong()
                            )
                        )
                    }
                })
        }
    }

    private fun setupSpinnerAdapter() {
        ArrayAdapter(requireContext(), R.layout.simple_spinner_item, viewModel.getPasswordTypes()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spPasswordType.adapter = this
        }
    }

    private fun observeGeneratedPassword() {
        viewModel.password.observe(viewLifecycleOwner, { value ->
            binding.tvPassword.text = value
            binding.ivCopy.isVisible = StringUtils.isNotBlank(value)
            binding.ivShare.isVisible = StringUtils.isNotBlank(value)
        })
    }

    private fun setupListeners() {
        binding.spPasswordType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.passwordType.value = adapterView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
            }
        }

        binding.etPasswordLength.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.passwordLength.value = s?.toString()
            }
        })

        binding.btGenerate.setOnClickListener {
            viewModel.password.value = StringUtils.EMPTY
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
            val message = viewModel.validateInputs(viewModel.passwordType.value, viewModel.passwordLength.value)
            if (StringUtils.isNotBlank(message)) {
                requireContext().showToast(message)
                return@setOnClickListener
            }
            generateAndCachePassword()
        }

        binding.ivCopy.setOnClickListener {
            requireContext().copyContentToClipboard(viewModel.password.value)
        }

        binding.ivShare.setOnClickListener {
            requireContext().shareContent(viewModel.password.value)
        }
    }

    private fun generateAndCachePassword() {
        val password = viewModel.generatePassword(
            viewModel.passwordType.value,
            viewModel.passwordLength.value?.toInt() ?: 0
        )
        viewModel.password.value = password
        val key = viewModel.getNextAvailableKey()
        val passwordData = PasswordData(key, password, Date().time)
        viewModel.cachePassword(passwordData)
    }
}