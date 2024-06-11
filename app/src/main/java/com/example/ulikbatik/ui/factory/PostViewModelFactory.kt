package com.example.ulikbatik.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.UserModel
import com.example.ulikbatik.data.repository.PostRepository
import com.example.ulikbatik.di.Injection
import com.example.ulikbatik.ui.dashboard.DashboardViewModel
import com.example.ulikbatik.ui.detailPost.DetailPostViewModel
import com.example.ulikbatik.ui.upload.UploadViewModel

class PostViewModelFactory(
    private val repository: PostRepository,
    private val pref: UserPreferences,
    private val userModel: UserModel?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            DashboardViewModel::class.java -> return DashboardViewModel(repository, pref, userModel) as T
            DetailPostViewModel::class.java -> return DetailPostViewModel(repository, pref, userModel) as T
            UploadViewModel::class.java -> return UploadViewModel(repository, userModel) as T
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
                Injection.providePreferences(context),
                Injection.provideUserModel(context)
            )
            return instance as PostViewModelFactory
        }
    }
}