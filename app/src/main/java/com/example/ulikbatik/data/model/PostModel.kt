package com.example.ulikbatik.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Url

data class PostModel(
    @SerializedName("USERID")
    val userId: String,

    @SerializedName("POSTID")
    val postId: String,

    @SerializedName("BATIKID")
    val batikId: String,

    @SerializedName("POSTIMG")
    val postImg: Url,

    @SerializedName("CAPTION")
    val caption: String,

    @SerializedName("LIKES")
    val likes: Int,

    @SerializedName("CREATEDAT")
    val createdAt: String,

    @SerializedName("UPDATEDAT")
    val updatedAt: String
)