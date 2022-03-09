package com.josycom.mayorjay.genpass.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast

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
        val clip = ClipData.newPlainText(label, content)
        clipboard.setPrimaryClip(clip)
    }

    fun shareContent(
        content: String,
        context: Context
    ) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, content)
        context.startActivity(Intent.createChooser(intent, "Share Via"))
    }
}