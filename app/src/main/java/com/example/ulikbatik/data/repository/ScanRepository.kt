package com.example.ulikbatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.response.ResultResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanRepository(
    private val apiService: ApiService
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun scanImage(attachment: MultipartBody.Part): LiveData<ResultResponse<BatikModel>> {
        _isLoading.value = true
        val responseJson = MutableLiveData<ResultResponse<BatikModel>>()
        val client = apiService.predict(attachment)
        client.enqueue(
            object : Callback<ResultResponse<BatikModel>> {
                override fun onResponse(
                    call: Call<ResultResponse<BatikModel>>,
                    response: Response<ResultResponse<BatikModel>>
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

                override fun onFailure(call: Call<ResultResponse<BatikModel>>, t: Throwable) {
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
        private var instance: ScanRepository? = null
        fun getInstance(
            apiService: ApiService
        ): ScanRepository =
            instance ?: synchronized(this) {
                instance ?: ScanRepository(apiService)
            }.also { instance = it }
    }
}