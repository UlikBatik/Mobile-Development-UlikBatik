package com.example.ulikbatik.data.model

import com.google.gson.annotations.SerializedName

class LikesModel (
    @SerializedName("USERID")
    val userId: String,

    @SerializedName("POSTID")
    val postId: String,

    @SerializedName("post")
    val post: PostModel

)