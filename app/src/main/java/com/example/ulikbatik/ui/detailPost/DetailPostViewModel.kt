package com.example.ulikbatik.ui.detailPost

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.repository.PostRepository

class DetailPostViewModel (
    private val postRepository: PostRepository
) : ViewModel() {

    fun getPost(postId: String): LiveData<GeneralResponse<PostModel>> {
        return postRepository.getPost(postId)
    }
}