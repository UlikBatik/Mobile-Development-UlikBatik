package com.example.ulikbatik.ui.detailPost

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.LikesResponse
import com.example.ulikbatik.data.repository.PostRepository

class DetailPostViewModel (
    private val postRepository: PostRepository
) : ViewModel() {

    fun getPost(postId: String): LiveData<GeneralResponse<PostModel>> {
        return postRepository.getPost(postId)
    }

    fun likePost(userId: String, postId: String): LiveData<LikesResponse>{
        return postRepository.likePost(userId,postId)
    }

    fun getLikes(userid: String): LiveData<GeneralResponse<List<LikesModel>>> {
        return postRepository.getLikes(userid)
    }
}