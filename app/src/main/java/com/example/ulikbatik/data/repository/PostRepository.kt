package com.example.ulikbatik.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.ulikbatik.data.remote.response.LikesBodyRequest
import com.example.ulikbatik.data.remote.response.LikesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepository(
    private val apiService: ApiService
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllPost(): LiveData<GeneralResponse<List<PostModel>>> {
        _isLoading.value = true
        val resultLiveData = MutableLiveData<GeneralResponse<List<PostModel>>>()
        val client = apiService.getAllPosts()
        client.enqueue(
            object : Callback<GeneralResponse<List<PostModel>>> {
                override fun onResponse(
                    call: Call<GeneralResponse<List<PostModel>>>,
                    response: Response<GeneralResponse<List<PostModel>>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    } else {
                        resultLiveData.value =
                            GeneralResponse(message = response.code().toString(), status = false)
                    }
                }

                override fun onFailure(call: Call<GeneralResponse<List<PostModel>>>, t: Throwable) {
                    _isLoading.value = false
                    resultLiveData.value =
                        GeneralResponse(message = "500",
                            status = false)
                }
            })
        return resultLiveData
    }

    fun getPost(postId: String): LiveData<GeneralResponse<PostModel>> {
        _isLoading.value = true
        val resultLiveData = MutableLiveData<GeneralResponse<PostModel>>()
        val client = apiService.getPost(postId)

        client.enqueue(object : Callback<GeneralResponse<PostModel>> {
            override fun onResponse(
                call: Call<GeneralResponse<PostModel>>,
                response: Response<GeneralResponse<PostModel>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    resultLiveData.value = response.body()
                } else {
                    resultLiveData.value = GeneralResponse(
                        message = response.code().toString(),
                        status = false
                    )
                }
            }

            override fun onFailure(call: Call<GeneralResponse<PostModel>>, t: Throwable) {
                _isLoading.value = false
                resultLiveData.value = GeneralResponse(
                    message = "500",
                    status = false
                )
            }
        })
        return resultLiveData
    }

    fun likePost(userId: String, postId: String): LiveData<LikesResponse> {
        val resultLiveData = MutableLiveData<LikesResponse>()
        val request = LikesBodyRequest(postId = postId)
        val client = apiService.likePost(userId, request)
        client.enqueue(
            object : Callback<LikesResponse> {
                override fun onResponse(
                    call: Call<LikesResponse>,
                    response: Response<LikesResponse>
                ) {
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    } else {
                        resultLiveData.value =
                            LikesResponse( message = response.code().toString(),
                                status = false)
                    }
                }

                override fun onFailure(call: Call<LikesResponse>, t: Throwable) {
                    resultLiveData.value =
                        LikesResponse(message = "500",
                            status = false)
                }
            })
        return resultLiveData
    }

    fun getLikes(userid: String): LiveData<GeneralResponse<List<LikesModel>>> {
        val resultLiveData = MutableLiveData<GeneralResponse<List<LikesModel>>>()
        val client = apiService.getLikes(userid)

        client.enqueue(object : Callback<GeneralResponse<List<LikesModel>>> {
            override fun onResponse(
                call: Call<GeneralResponse<List<LikesModel>>>,
                response: Response<GeneralResponse<List<LikesModel>>>
            ) {
                if (response.isSuccessful) {
                    resultLiveData.value = response.body()
                } else {
                    resultLiveData.value = GeneralResponse(
                        message = response.code().toString(),
                        status = false
                    )
                }
            }

            override fun onFailure(call: Call<GeneralResponse<List<LikesModel>>>, t: Throwable) {
                resultLiveData.value = GeneralResponse(
                    message = "500",
                    status = false
                )
            }
        })
        return resultLiveData
    }

    fun createPost(
        image: MultipartBody.Part,
        caption: RequestBody,
        userId: RequestBody,
        batikId: RequestBody
    ): LiveData<GeneralResponse<PostResponse>> {
        _isLoading.value = true
        val resultLiveData = MutableLiveData<GeneralResponse<PostResponse>>()
        val client = apiService.createPost(image, caption, userId, batikId)
        client.enqueue(
            object : Callback<GeneralResponse<PostResponse>> {
                override fun onResponse(
                    call: Call<GeneralResponse<PostResponse>>,
                    response: Response<GeneralResponse<PostResponse>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    } else {
                        resultLiveData.value = GeneralResponse(
                            message = response.code().toString(),
                            status = false
                        )
                    }
                }

                override fun onFailure(call: Call<GeneralResponse<PostResponse>>, t: Throwable) {
                    _isLoading.value = false
                    resultLiveData.value = GeneralResponse(
                        message = "500",
                        status = false
                    )
                }
            }
        )

        return resultLiveData
    }

    fun deletePost(idPost: String): LiveData<GeneralResponse<PostModel>> {
        _isLoading.value = true
        val resultLiveData = MutableLiveData<GeneralResponse<PostModel>>()
        val client = apiService.deletePost(idPost)
        client.enqueue(
            object : Callback<GeneralResponse<PostModel>> {
                override fun onResponse(
                    call: Call<GeneralResponse<PostModel>>,
                    response: Response<GeneralResponse<PostModel>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    } else {
                        resultLiveData.value = GeneralResponse(
                            message = response.code().toString(),
                            status = false
                        )
                    }
                }

                override fun onFailure(call: Call<GeneralResponse<PostModel>>, t: Throwable) {
                    _isLoading.value = false
                    resultLiveData.value = GeneralResponse(
                        message = "500",
                        status = false
                    )
                }
            }
        )
        return resultLiveData
    }


    companion object {
        fun getInstance(
            apiService: ApiService
        ) = PostRepository(apiService)
    }
}