package com.example.ulikbatik.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.request.LoginBodyRequest
import com.example.ulikbatik.data.remote.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService,
) {

    fun login(email: String, password: String): LiveData<LoginResponse> {
        val resultLiveData = MutableLiveData<LoginResponse>()
        val reqBody = LoginBodyRequest(email = email, password = password)
        val client = apiService.login(reqBody)
        client.enqueue(
            object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                    }else{
                        resultLiveData.value =
                            LoginResponse(message = response.code().toString(), status = false)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    resultLiveData.value =
                        LoginResponse(message = t.message.toString(), status = false)
                }
            }
        )

        return resultLiveData
    }


    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AuthRepository =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(apiService)
            }.also { instance = it }
    }
}