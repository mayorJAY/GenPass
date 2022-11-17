package com.josycom.mayorjay.genpass.view.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.josycom.mayorjay.genpass.data.models.PasswordData
import com.josycom.mayorjay.genpass.databinding.FragmentPasswordListBinding
import com.josycom.mayorjay.genpass.viewmodel.PasswordListViewModel
import com.josycom.mayorjay.genpass.viewmodel.PasswordListViewModelFactory
import com.josycom.mayorjay.genpass.data.preferencedatastore.PreferenceDataSourceImpl
import com.josycom.mayorjay.genpass.data.preferencedatastore.dataStore
import com.josycom.mayorjay.genpass.data.repository.DataRepositoryImpl
import com.josycom.mayorjay.genpass.util.Constants
import com.josycom.mayorjay.genpass.util.copyContentToClipboard
import com.josycom.mayorjay.genpass.util.shareContent
import org.apache.commons.lang3.StringUtils

class PasswordListFragment : Fragment() {

    private val viewModel: PasswordListViewModel by viewModels {
        val dataSource = PreferenceDataSourceImpl(requireContext().dataStore)
        val repository = DataRepositoryImpl(dataSource)
        PasswordListViewModelFactory(repository)
    }
    private lateinit var binding: FragmentPasswordListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrievePasswords()
        displayPasswords()
    }

    private fun retrievePasswords() {
        for (item in Constants.PASSWORD_KEY_LIST) {
            viewModel.getPasswordPref(item).observe(viewLifecycleOwner) { value ->
                val iterator = viewModel.tempPasswordList.listIterator()
                while (iterator.hasNext()) {
                    if (StringUtils.equalsIgnoreCase(iterator.next().key, item)) {
                        iterator.remove()
                    }
                }
                if (value != null && StringUtils.isNotBlank(value)) {
                    viewModel.tempPasswordList.add(
                        PasswordData(
                            item,
                            value.substringBefore("-"),
                            value.substringAfter("-").toLong()
                        )
                    )
                    viewModel.tempPasswordList.sortWith { p0, p1 ->
                        (p0?.timeGenerated ?: 0L).compareTo(p1?.timeGenerated ?: 0L)
                    }
                    viewModel.passwordList.value = viewModel.tempPasswordList
                }
            }
        }
    }

    fun displayPasswords() {
        val passwordListAdapter = PasswordListAdapter()
        binding.rvPasswords.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = passwordListAdapter
        }
        viewModel.passwordList.observe(viewLifecycleOwner) { list ->
            binding.tvImage.isVisible = list.isEmpty()
            binding.tvInfoDisplay.isVisible = list.isEmpty()
            passwordListAdapter.setData(list.reversed())
        }
        passwordListAdapter.setOnClickListeners(copyListener, shareListener)
    }

    private val copyListener = View.OnClickListener { v ->
        val password = v?.tag as String
        requireContext().copyContentToClipboard(password)
    }

    private val shareListener = View.OnClickListener { v ->
        val password = v?.tag as String
        requireContext().shareContent(password)
    }
}