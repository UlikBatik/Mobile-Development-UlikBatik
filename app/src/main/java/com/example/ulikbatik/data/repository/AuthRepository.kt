package com.example.ulikbatik.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ulikbatik.data.remote.config.ApiService
import com.example.ulikbatik.data.remote.request.LoginBodyRequest
import com.example.ulikbatik.data.remote.response.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(
    private val apiService: ApiService
) {

    fun login(email: String, password: String): LiveData<AuthResponse> {
        val resultLiveData = MutableLiveData<AuthResponse>()
        val reqBody = LoginBodyRequest(email= email, password = password)
        val client = apiService.login(reqBody)
        client.enqueue(
            object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful) {
                        resultLiveData.value = response.body()
                        Log.d("AuthRepository", response.body()?.token.toString())
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    //logic failed
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