package com.example.ulikbatik.ui.dashboard

import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.local.UserPreferences
import com.example.ulikbatik.data.repository.PostRepository

class DashboardViewModel (
    private val postRepository: PostRepository,
    preferences : UserPreferences
) : ViewModel() {


    var pref = preferences

    val isLoading = postRepository.isLoading
    val allPost = postRepository.getAllPost()

}