package com.example.ulikbatik.data.remote.response

import com.google.gson.annotations.SerializedName

data class LikesResponse (
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean
)

data class LikesBodyRequest(
    @SerializedName("POSTID")
    val postId: String
)