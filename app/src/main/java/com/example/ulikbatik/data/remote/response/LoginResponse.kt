package com.example.ulikbatik.data.remote.response

import com.example.ulikbatik.data.model.UserModel
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: UserModel? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("token")
    val token: String? = null
)
