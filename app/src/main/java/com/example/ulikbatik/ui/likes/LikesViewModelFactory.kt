package com.example.ulikbatik.ui.likes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.repository.LikesRepository
import com.example.ulikbatik.di.Injection

class LikesViewModelFactory  (
    private val repository: LikesRepository
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            LikesViewModel::class.java -> return LikesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: LikesViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): LikesViewModelFactory {
            instance = LikesViewModelFactory(Injection.provideLikesRepository(context))
            return instance as LikesViewModelFactory
        }
    }
}