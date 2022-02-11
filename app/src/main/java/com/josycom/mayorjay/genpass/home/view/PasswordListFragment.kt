package com.josycom.mayorjay.genpass.home.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.josycom.mayorjay.genpass.R

class PasswordListFragment : Fragment() {

    private lateinit var viewModelPassword: PasswordListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelPassword = ViewModelProviders.of(this).get(PasswordListViewModel::class.java)
    }

}