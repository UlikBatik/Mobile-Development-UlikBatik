package com.example.ulikbatik.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.remote.response.AuthResponse
import com.example.ulikbatik.data.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun login(email: String, password: String): LiveData<AuthResponse> {
       return authRepository.login(email, password)
    }

}