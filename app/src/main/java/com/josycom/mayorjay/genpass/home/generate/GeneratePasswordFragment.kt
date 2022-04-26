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
import com.josycom.mayorjay.genpass.util.Utilities
import org.apache.commons.lang3.StringUtils
import java.util.Date


class GeneratePasswordFragment : Fragment() {

    private val viewModel: GeneratePasswordViewModel by viewModels()
    private lateinit var binding: FragmentGeneratePasswordBinding
    private var preferenceManager: PreferenceManager? = null
    private var queueList = listOf<PasswordData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeneratePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = PreferenceManager(requireContext().dataStore)
        observePasswordPref()
        setupSpinnerAdapter()
        setupListeners()
        observeGeneratedPassword()
        //Todo: Implement app review prompt
    }

    private fun observePasswordPref() {
        for (key in preferenceManager?.list ?: listOf()) {
            preferenceManager?.getPasswordPrefFlow(key)?.asLiveData()?.observe(
                viewLifecycleOwner,
                { value ->
                    queueList = viewModel.queue.toList()
                    val passList = mutableListOf<String>()
                    for (item in queueList) {
                        passList.add("${item.password}-${item.timeGenerated}")
                    }
                    if (value != null && StringUtils.isNotBlank(value) && !passList.contains(value)) {
                        viewModel.queue.add(
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
            val message = viewModel.validateInputs()
            if (StringUtils.isNotBlank(message)) {
                Utilities.showToast(message, requireContext())
                return@setOnClickListener
            }
            generateAndCachePassword()
        }

        binding.ivCopy.setOnClickListener {
            Utilities.copyContentToClipboard(
                viewModel.password.value ?: StringUtils.EMPTY,
                requireContext()
            )
        }

        binding.ivShare.setOnClickListener {
            Utilities.shareContent(viewModel.password.value ?: StringUtils.EMPTY, requireContext())
        }
    }

    private fun generateAndCachePassword() {
        val password = viewModel.generatePassword(
            viewModel.passwordType.value ?: StringUtils.EMPTY,
            viewModel.passwordLength.value?.toInt() ?: 0
        )
        viewModel.password.value = password
        val time = Date().time
        if (viewModel.queue.size >= 10) {
            viewModel.queue.remove()
        }
        queueList = viewModel.queue.toList()
        var key = StringUtils.EMPTY
        val keyList = mutableListOf<String>()
        for (item in queueList) {
            keyList.add(item.key)
        }
        for (item in preferenceManager?.list ?: listOf()) {
            if (!keyList.contains(item)) {
                key = item
                break
            }
        }
        val passwordData = PasswordData(key, password, time)
        viewModel.cachePassword(requireContext(), passwordData)
    }
}