package com.josycom.mayorjay.genpass.view.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.genpass.data.models.PasswordData
import com.josycom.mayorjay.genpass.databinding.PasswordListItemBinding
import com.josycom.mayorjay.genpass.util.getFormattedDate

class PasswordListAdapter : RecyclerView.Adapter<PasswordListAdapter.PasswordViewHolder>() {

    private var passwordData: List<PasswordData> = listOf()
    companion object {
        private var copyClickListener: View.OnClickListener? = null
        private var shareClickListener: View.OnClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val binding = PasswordListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PasswordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        holder.bind(passwordData[position])
    }

    override fun getItemCount(): Int = passwordData.size

    fun setData(passwordList: List<PasswordData>) {
        passwordData = passwordList
    }

    fun setOnClickListeners(copyOnClickListener: View.OnClickListener, shareOnClickListener: View.OnClickListener) {
        copyClickListener = copyOnClickListener
        shareClickListener = shareOnClickListener
    }

    class PasswordViewHolder(private val binding: PasswordListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivCopy.setOnClickListener(copyClickListener)
            binding.ivShare.setOnClickListener(shareClickListener)
        }

        fun bind(passwordData: PasswordData) {
            binding.apply {
                tvDate.text = passwordData.timeGenerated.getFormattedDate()
                passwordData.password.apply {
                    tvPassword.text = this
                    ivCopy.tag = this
                    ivShare.tag = this
                }
            }
        }
    }
}