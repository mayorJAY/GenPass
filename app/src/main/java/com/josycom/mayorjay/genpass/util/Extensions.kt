package com.josycom.mayorjay.genpass.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.josycom.mayorjay.genpass.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.copyContentToClipboard(content: String?) {
    val clipboard = this.getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
    val label = "New Password"
    val clipData = ClipData.newPlainText(label, content)
    if (clipData != null) {
        clipboard.setPrimaryClip(clipData)
    }
    this.showToast(this.getString(R.string.copied_to_clipboard))
}

fun Context.shareContent(content: String?) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, content)
    }
    this.startActivity(Intent.createChooser(intent, "Share Via"))
}

fun Long.getFormattedDate(): String? {
    val simpleDateFormat = SimpleDateFormat("E dd-MMM-yyyy HH:mm:ss", Locale.getDefault())
    return simpleDateFormat.format(Date(this))
}

fun View.showSnackBar(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT,
    actionText: String,
    actionColor: Int,
    actionCallBack: () -> Unit
) {
    Snackbar.make(this, message, length).apply {
        setActionTextColor(actionColor)
        setAction(actionText) {
            actionCallBack()
        }
    }.show()
}