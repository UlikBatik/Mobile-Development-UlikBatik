package com.example.ulikbatik.data.remote.response

import com.google.gson.annotations.SerializedName

data class GeneralResponse<T>(
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
)
