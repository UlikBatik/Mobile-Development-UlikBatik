package com.example.ulikbatik.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.Post
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.response.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepository(
    private val apiService: ApiService
) {

    fun getAllPost(): LiveData<GeneralResponse<List<PostModel>>>{
        val resultLiveData = MutableLiveData<GeneralResponse<List<PostModel>>>()
        val client = apiService.getAllPosts()
        client.enqueue(
            object : Callback<GeneralResponse<List<PostModel>>> {
                override fun onResponse(
                    call: Call<GeneralResponse<List<PostModel>>>,
                    response: Response<GeneralResponse<List<PostModel>>>
                ) {
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    } else {
                        resultLiveData.value =
                            GeneralResponse(message = response.code().toString(), status = false)
                    }
                }

                override fun onFailure(call: Call<GeneralResponse<List<PostModel>>>, t: Throwable) {
                    resultLiveData.value =
                        GeneralResponse(message = t.message.toString(), status = false)
                }
            })
        return resultLiveData
    }

    fun getPost(postId: String): LiveData<GeneralResponse<PostModel>> {
        val resultLiveData = MutableLiveData<GeneralResponse<PostModel>>()
        val client = apiService.getPost(postId)

        client.enqueue(object : Callback<GeneralResponse<PostModel>> {
            override fun onResponse(
                call: Call<GeneralResponse<PostModel>>,
                response: Response<GeneralResponse<PostModel>>
            ) {
                if (response.isSuccessful) {
                    resultLiveData.value = response.body()
                } else {
                    resultLiveData.value = GeneralResponse(
                        message = "Error: ${response.code()} ${response.message()}",
                        status = false
                    )
                }
            }
            override fun onFailure(call: Call<GeneralResponse<PostModel>>, t: Throwable) {
                resultLiveData.value = GeneralResponse(
                    message = "Failure: ${t.message}",
                    status = false
                )
            }
        })
        return resultLiveData
    }

    companion object {
        fun getInstance(
            apiService: ApiService
        ) = PostRepository(apiService)
    }
}