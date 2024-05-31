package com.example.ulikbatik.data.repository


import com.example.ulikbatik.data.remote.config.ApiService

class PostRepository(
    private val apiService: ApiService
) {

//    fun createPost(image: File, caption: String, userId: String, batikId: String) {
//        val client = apiService.createPost(image= image, caption, userId, batikId))
//    }


    companion object {
        fun getInstance(
            apiService: ApiService
        ) = PostRepository(apiService)
    }
}