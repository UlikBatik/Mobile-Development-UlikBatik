package com.example.ulikbatik.utils.helper

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarBuilder {

    fun showSnackBarInfo(message: String, contextView: View) {
        Snackbar.make(contextView, message, Snackbar.LENGTH_SHORT)
            .show()
    }


    fun showSnackBarAction(
        message: String,
        actionName: String,
        contextView: View,
        callback: (Boolean) -> Unit
    ) {
        Snackbar.make(contextView, message, Snackbar.LENGTH_LONG)
            .setAction(
                actionName
            ) {
                callback(true)
            }
            .show()
    }

}