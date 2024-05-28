package com.example.ulikbatik.data.remote.config

import com.example.ulikbatik.data.remote.request.LoginBodyRequest
import com.example.ulikbatik.data.remote.request.RegisterBodyRequest
import com.example.ulikbatik.data.remote.response.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(
        @Body loginBodyRequest: LoginBodyRequest
    ): Call<AuthResponse>

    @POST("register")
    fun register(
        @Body registerBodyRequest: RegisterBodyRequest
    ): Call<AuthResponse>
}