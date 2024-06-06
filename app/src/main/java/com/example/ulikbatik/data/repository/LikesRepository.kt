package com.example.ulikbatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.response.GeneralResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LikesRepository(
    private val apiService: ApiService
) {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun getLikes(userid: String): LiveData<GeneralResponse<List<LikesModel>>> {
        _isLoading.value = true
        val resultLiveData = MutableLiveData<GeneralResponse<List<LikesModel>>>()
        val client = apiService.getLikes(userid)

        client.enqueue(object : Callback<GeneralResponse<List<LikesModel>>> {
            override fun onResponse(
                call: Call<GeneralResponse<List<LikesModel>>>,
                response: Response<GeneralResponse<List<LikesModel>>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    resultLiveData.value = response.body()
                } else {
                    resultLiveData.value = GeneralResponse(
                        message = "Error: ${response.code()} ${response.message()}",
                        status = false
                    )
                }
            }
            override fun onFailure(call: Call<GeneralResponse<List<LikesModel>>>, t: Throwable) {
                _isLoading.value = false
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
        ) = LikesRepository(apiService)
    }
}