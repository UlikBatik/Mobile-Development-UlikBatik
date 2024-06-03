package com.example.ulikbatik.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.response.ScanResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanRepository(
    private val apiService: ApiService
) {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun scanImage(attachment: MultipartBody.Part): LiveData<ScanResponse> {
        _isLoading.value = true
        val responseJson = MutableLiveData<ScanResponse>()
        val client = apiService.predict(attachment)
        client.enqueue(
            object : Callback<ScanResponse> {
                override fun onResponse(
                    call: Call<ScanResponse>,
                    response: Response<ScanResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        responseJson.value = response.body()
                    }
                }

                override fun onFailure(call: Call<ScanResponse>, t: Throwable) {
                    TODO("Not yet implemented")
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