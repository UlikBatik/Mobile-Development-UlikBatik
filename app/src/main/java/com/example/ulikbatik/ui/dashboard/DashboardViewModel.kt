package com.example.ulikbatik.ui.dashboard


import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.repository.PostRepository

class DashboardViewModel(
    postRepository: PostRepository,
    userId: String?
) : ViewModel() {

    val userIdData = userId

    val isLoading = postRepository.isLoading
    val allPost = postRepository.getAllPost()

}