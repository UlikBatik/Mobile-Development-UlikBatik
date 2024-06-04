package com.example.ulikbatik.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.LoginResponse
import com.example.ulikbatik.data.repository.AuthRepository
import com.example.ulikbatik.data.repository.PostRepository

class DashboardViewModel (
    private val postRepository: PostRepository
) : ViewModel() {

    fun getPost(): LiveData<GeneralResponse<List<PostModel>>> {
        return postRepository.getPost()
    }

}