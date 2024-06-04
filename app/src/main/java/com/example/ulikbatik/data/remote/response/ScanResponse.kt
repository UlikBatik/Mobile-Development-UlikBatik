package com.example.ulikbatik.data.remote.response

import com.example.ulikbatik.data.model.BatikModel
import com.google.gson.annotations.SerializedName

data class ScanResponse(

    @field:SerializedName("result")
    val result: BatikModel? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Boolean
)

