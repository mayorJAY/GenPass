package com.josycom.mayorjay.genpass.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.josycom.mayorjay.genpass.data.PasswordData
import com.josycom.mayorjay.genpass.databinding.FragmentPasswordListBinding
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import com.josycom.mayorjay.genpass.util.Utilities
import org.apache.commons.lang3.StringUtils

class PasswordListFragment : Fragment() {

    private val viewModel: PasswordListViewModel by viewModels()
    private lateinit var binding: FragmentPasswordListBinding
    private val passwordList = mutableListOf<PasswordData>()

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
        val dataStore = PreferenceManager(requireContext().dataStore)
        for (item in dataStore.list) {
            dataStore.getPasswordPrefFlow(item).asLiveData().observe(viewLifecycleOwner, { value ->
                val iterator = passwordList.listIterator()
                while (iterator.hasNext()) {
                    if (StringUtils.equalsIgnoreCase(iterator.next().key , item)) {
                        iterator.remove()
                    }
                }
                if (value != null && StringUtils.isNotBlank(value)) {
                    passwordList.add(PasswordData(item, value.substringBefore("-"), value.substringAfter("-").toLong()))
                    passwordList.sortWith { p0, p1 -> (p0?.timeGenerated ?: 0L).compareTo(p1?.timeGenerated ?: 0L) }
                    viewModel.passwordList.value = passwordList
                }
            })
        }
    }

    fun displayPasswords() {
        val passwordListAdapter = PasswordListAdapter()
        binding.rvPasswords.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = passwordListAdapter
        }
        viewModel.passwordList.observe(viewLifecycleOwner, { list ->
            binding.tvImage.isVisible = list.isEmpty()
            binding.tvOopsDisplay.isVisible = list.isEmpty()
            passwordListAdapter.setData(list.reversed())
        })
        passwordListAdapter.setOnClickListeners(copyListener, shareListener)
    }

    private val copyListener = View.OnClickListener { v ->
        val password = v?.tag as String
        Utilities.copyContentToClipboard(password, requireContext())
    }

    private val shareListener = View.OnClickListener { v ->
        val password = v?.tag as String
        Utilities.shareContent(password, requireContext())
    }
}