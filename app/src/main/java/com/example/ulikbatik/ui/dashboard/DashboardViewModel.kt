package com.example.ulikbatik.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.repository.PostRepository

class DashboardViewModel(
    private val postRepository: PostRepository,
    preferences: UserPreferences,
    userId: String?
) : ViewModel() {

    val userIdData = userId
    var pref = preferences
    val isLoading = postRepository.isLoading

    fun getPosts(): LiveData<GeneralResponse<List<PostModel>>> {
        return postRepository.getAllPost()
    }

}