package com.example.ulikbatik.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.repository.AuthRepository
import com.example.ulikbatik.di.Injection

class AuthViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            AuthViewModel::class.java -> return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: AuthViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): AuthViewModelFactory {
            instance ?: synchronized(AuthViewModelFactory::class.java) {
                instance = AuthViewModelFactory(Injection.provideAuthRepository(context))
            }
            return instance as AuthViewModelFactory
        }
    }
}