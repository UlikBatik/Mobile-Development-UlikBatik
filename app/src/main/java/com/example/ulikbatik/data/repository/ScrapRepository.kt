package com.example.ulikbatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.response.ResultResponse
import com.example.ulikbatik.data.remote.response.ScrapperResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapRepository(
   private val apiService: ApiService
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getScrapperData(query:String): LiveData<ResultResponse<List<ScrapperResponse>>> {
        _isLoading.value = true
        val responseJson = MutableLiveData<ResultResponse<List<ScrapperResponse>>>()
        val queryBatik = "Batik $query"
        val client = apiService.searchContent(queryBatik)
        client.enqueue(
            object: Callback<ResultResponse<List<ScrapperResponse>>> {
                override fun onResponse(
                    call: Call<ResultResponse<List<ScrapperResponse>>>,
                    response: Response<ResultResponse<List<ScrapperResponse>>>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        responseJson.value = response.body()
                    } else {
                        responseJson.value = ResultResponse(
                            message = response.code().toString(),
                            status = false
                        )
                    }
                }

                override fun onFailure(
                    call: Call<ResultResponse<List<ScrapperResponse>>>,
                    t: Throwable
                ) {
                    _isLoading.value = false
                    responseJson.value = ResultResponse(
                        message = "500",
                        status = false
                    )
                }
            }
        )
        return responseJson
    }

    companion object {
        @Volatile
        private var instance: ScrapRepository? = null
        fun getInstance(
            apiService: ApiService
        ): ScrapRepository =
            instance ?: synchronized(this) {
                instance ?: ScrapRepository(apiService)
            }.also { instance = it }
    }
}