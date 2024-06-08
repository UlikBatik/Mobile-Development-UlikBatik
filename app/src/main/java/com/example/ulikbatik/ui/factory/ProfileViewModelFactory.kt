package com.example.ulikbatik.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.repository.UserRepository
import com.example.ulikbatik.di.Injection
import com.example.ulikbatik.ui.profile.ProfileViewModel

class ProfileViewModelFactory(
    private val userRepository: UserRepository,
    private val userId: String?
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            ProfileViewModel::class.java -> return ProfileViewModel(userRepository, userId)  as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: ProfileViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ProfileViewModelFactory {
            instance = ProfileViewModelFactory(
                Injection.provideUserRepository(context),
                Injection.provideUserId(context)
            )
            return instance as ProfileViewModelFactory
        }
    }
}