package com.example.ulikbatik.data.remote.response

import com.google.gson.annotations.SerializedName

data class ScanResponse(

    @field:SerializedName("result")
    val result: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Boolean
)
