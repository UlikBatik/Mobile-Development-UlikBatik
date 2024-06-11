package com.example.ulikbatik.utils.helper

import android.content.Context
import com.example.ulikbatik.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogBuilder {

    fun askDialog(context: Context, title: String, message: String, callback: (Boolean) -> Unit) {
        MaterialAlertDialogBuilder(context,R.style.ThemeOverlay_App_MaterialAlertDialog)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(context.getString(R.string.cancel)) { _, _ ->
                callback(false)
            }
            .setPositiveButton(context.getString(R.string.ok)) { _, _ ->
                callback(true)
            }
            .show()
    }
}