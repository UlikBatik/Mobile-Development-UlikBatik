package com.example.ulikbatik.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResultResponse<T>(

    @field:SerializedName("result")
    val result: T? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Boolean
)

