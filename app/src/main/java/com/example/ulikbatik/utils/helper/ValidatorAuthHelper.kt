package com.example.ulikbatik.utils.helper

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

object ValidatorAuthHelper {

    fun showToast(context: Context, msg: String) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 8
    }

    fun confirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }


    fun onChangeClear(edt: EditText, edtLayout:TextInputLayout) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { edtLayout.error = null
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                edtLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {
                edtLayout.error = null
            }
        })
    }
}