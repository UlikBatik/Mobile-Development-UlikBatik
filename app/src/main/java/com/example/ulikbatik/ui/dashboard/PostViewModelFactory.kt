package com.example.ulikbatik.ui.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.repository.PostRepository
import com.example.ulikbatik.di.Injection
import com.example.ulikbatik.ui.auth.AuthViewModelFactory

class PostViewModelFactory (
    private val repository: PostRepository
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            DashboardViewModel::class.java -> return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: PostViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): PostViewModelFactory {
            instance = PostViewModelFactory(Injection.providePostRepository(context))
            return instance as PostViewModelFactory
        }
    }
}