package com.josycom.mayorjay.genpass.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.josycom.mayorjay.genpass.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utilities {

    fun showToast(
        text: String,
        context: Context
    ) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun copyContentToClipboard(
        content: String,
        context: Context
    ) {
        val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
        val label = "New Password"
        val clipData = ClipData.newPlainText(label, content)
        if (clipData != null) {
            clipboard.setPrimaryClip(clipData)
        }
        showToast(context.getString(R.string.copied_to_clipboard), context)
    }

    fun shareContent(
        content: String,
        context: Context
    ) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, content)
            context.startActivity(Intent.createChooser(this, "Share Via"))
        }
    }

    fun getFormattedDate(time: Long): String? {
        val date = Date(time)
        val simpleDateFormat = SimpleDateFormat("E dd/MMM/yyyy HH:mm", Locale.getDefault())
        return simpleDateFormat.format(date)
    }
}