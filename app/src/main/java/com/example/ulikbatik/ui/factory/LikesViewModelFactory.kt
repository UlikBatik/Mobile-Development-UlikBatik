package com.example.ulikbatik.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.repository.LikesRepository
import com.example.ulikbatik.di.Injection
import com.example.ulikbatik.ui.likes.LikesViewModel

class LikesViewModelFactory  (
    private val repository: LikesRepository,
    private val userModel: UserModel?
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            LikesViewModel::class.java -> return LikesViewModel(repository,userModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: LikesViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): LikesViewModelFactory {
            instance = LikesViewModelFactory(
                Injection.provideLikesRepository(context),
                Injection.provideUserModel(context)
            )
            return instance as LikesViewModelFactory
        }
    }
}