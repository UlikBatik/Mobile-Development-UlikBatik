package com.example.ulikbatik.ui.customView



import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.ulikbatik.R


class CustomDialog(
    private val context: Context,
) {
    fun showDialog(
        title: String, message: String, callback: (Boolean) -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.item_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val titleTv = dialog.findViewById<TextView>(R.id.dialog_title)
        val descTv = dialog.findViewById<TextView>(R.id.dialog_desc)
        val btnCancel = dialog.findViewById<Button>(R.id.cancel_btn)
        val btnConfirm = dialog.findViewById<Button>(R.id.confirm_btn)

        titleTv.text = title
        descTv.text = message

        btnCancel.setOnClickListener {
            dialog.dismiss()
            callback(false)
        }

        btnConfirm.setOnClickListener {
            dialog.dismiss()  // Dismiss the dialog first
            callback(true)
        }

        dialog.show()
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }


}