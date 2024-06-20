package com.example.ulikbatik.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.remote.response.LoginResponse
import com.example.ulikbatik.data.remote.response.RegisterResponse
import com.example.ulikbatik.data.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository,
    preferences: UserPreferences
) : ViewModel() {

    var pref = preferences
    val isLoading = authRepository.isLoading

    fun login(email: String, password: String): LiveData<LoginResponse> {
        return authRepository.login(email, password)
    }

    fun register(username: String, email: String, password: String, confirmPassword: String): LiveData<RegisterResponse>{
        return authRepository.register(username,email, password, confirmPassword)
    }
}