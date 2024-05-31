package com.example.ulikbatik.utils.helper

import android.content.Context
import android.util.Patterns
import android.widget.Toast

object Helper {

    fun showToast(context: Context, msg: String) {
        Toast.makeText(
            context,
            msg,
            Toast.LENGTH_LONG).show()
    }

    fun validateEmail(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean{
        return password.length >= 8
    }

    fun confirmPassword(password: String, confirmPassword: String): Boolean{
        return password == confirmPassword
    }
}