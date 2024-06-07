package com.example.ulikbatik.data.remote.config

import com.example.ulikbatik.data.model.PostModel
import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.model.LikesModel
import com.example.ulikbatik.data.remote.request.LoginBodyRequest
import com.example.ulikbatik.data.remote.request.RegisterBodyRequest
import com.example.ulikbatik.data.remote.response.GeneralResponse
import com.example.ulikbatik.data.remote.response.LikesBodyRequest
import com.example.ulikbatik.data.remote.response.LikesResponse
import com.example.ulikbatik.data.remote.response.LoginResponse
import com.example.ulikbatik.data.remote.response.PostResponse
import com.example.ulikbatik.data.remote.response.RegisterResponse
import com.example.ulikbatik.data.remote.response.ScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("posts")
    fun getAllPosts(): Call<GeneralResponse<List<PostModel>>>

    @GET("post/{postID}")
    fun getPost(
        @Path("postID") postId: String
    ): Call<GeneralResponse<PostModel>>

    @POST("login")
    fun login(
        @Body loginBodyRequest: LoginBodyRequest
    ): Call<LoginResponse>

    @POST("register")
    fun register(
        @Body registerBodyRequest: RegisterBodyRequest
    ): Call<RegisterResponse>

    @POST("posts")
    @Multipart
    fun createPost(
        @Part IMAGE: MultipartBody.Part,
        @Part("CAPTION") caption: RequestBody,
        @Part("USERID") userId: RequestBody,
        @Part("BATIKID") batikId: RequestBody
    ): Call<GeneralResponse<PostResponse>>

    @POST("predict")
    @Multipart
    fun predict(
        @Part attachment: MultipartBody.Part
    ): Call<ScanResponse>

    @GET("batiks")
    fun getBatik(): Call<GeneralResponse<List<BatikModel>>>

    @GET("batik/{batikId}")
    fun getDetailBatik(
        @Path("batikId") batikId: String
    ): Call<GeneralResponse<BatikModel>>

    @GET("likes/{userId}")
    fun getLikes(
        @Path("userId") userid: String
    ): Call<GeneralResponse<List<LikesModel>>>

    @POST("like/{userId}")
    fun likePost(
        @Path("userId") userid: String,
        @Body postId: LikesBodyRequest
    ): Call<LikesResponse>
}