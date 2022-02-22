package com.josycom.mayorjay.genpass.home.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.josycom.mayorjay.genpass.data.PasswordData
import com.josycom.mayorjay.genpass.databinding.FragmentPasswordListBinding
import com.josycom.mayorjay.genpass.persistence.PreferenceManager
import com.josycom.mayorjay.genpass.persistence.dataStore
import org.apache.commons.lang3.StringUtils
import java.sql.Timestamp

class PasswordListFragment : Fragment() {

    private val viewModel: PasswordListViewModel by viewModels()
    private lateinit var binding: FragmentPasswordListBinding
    private val list = mutableListOf<String>()
    private val listOf = mutableListOf<PasswordData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this)[PasswordListViewModel::class.java]

        val dataStore = PreferenceManager(requireContext().dataStore)
        for (item in dataStore.list) {
            dataStore.getPasswordPrefFlow(item).asLiveData().observe(viewLifecycleOwner, { value ->
                val iterator = listOf.listIterator()
                while (iterator.hasNext()) {
                    if (StringUtils.equalsIgnoreCase(iterator.next().key , item)) {
                        iterator.remove()
                    }
                }
                if (value != null && StringUtils.isNotBlank(value)) {
                    listOf.add(PasswordData(item, value.substringBefore("-"), value.substringAfter("-").toLong()))
                }
            })
        }

        binding.textView.setOnClickListener {
            displayPasswords()
        }
    }

    private fun displayPasswords() {
        listOf.sortWith { p0, p1 -> (p0?.timeGenerated ?: 0L).compareTo(p1?.timeGenerated ?: 0L) }
        listOf.forEach {
            val value = "${Timestamp(it.timeGenerated)}-->${it.password}"
            if (!list.contains(value)) {
                list.add(value)
            }
        }
        val dialog = AlertDialog.Builder(requireContext())
        list.reverse()
        dialog.setItems(list.toTypedArray(), null)
        dialog.show()
    }

}