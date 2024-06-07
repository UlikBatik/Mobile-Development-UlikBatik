package com.example.ulikbatik.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.repository.PostRepository
import com.example.ulikbatik.di.Injection
import com.example.ulikbatik.ui.dashboard.DashboardViewModel
import com.example.ulikbatik.ui.detailPost.DetailPostViewModel
import com.example.ulikbatik.ui.upload.UploadViewModel

class PostViewModelFactory(
    private val repository: PostRepository,
    private val userId: String?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            DashboardViewModel::class.java -> return DashboardViewModel(repository) as T
            DetailPostViewModel::class.java -> return DetailPostViewModel(repository) as T
            UploadViewModel::class.java -> return UploadViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: PostViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): PostViewModelFactory {
            instance = PostViewModelFactory(
                Injection.providePostRepository(context),
                Injection.provideUserId(context)
            )
            return instance as PostViewModelFactory
        }
    }
}