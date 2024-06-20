package com.example.ulikbatik.data.remote.response

import com.example.ulikbatik.data.model.BatikModel
import com.google.gson.annotations.SerializedName

data class ProfileUserResponse(

    @field:SerializedName("data")
    val data: DataProfile? = null,

    @field:SerializedName("likesReceived")
    val likesReceived: Int? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Boolean
)

data class DataProfile(

    @field:SerializedName("_count")
    val count: Count? = null,

    @field:SerializedName("post")
    val post: List<PostItem>,

    @field:SerializedName("USERID")
    val uSERID: String,

    @field:SerializedName("USERNAME")
    val uSERNAME: String,

    @field:SerializedName("PROFILEIMG")
    val pROFILEIMG: String? = null,

    @field:SerializedName("EMAIL")
    val eMAIL: String
)

data class Count(

    @field:SerializedName("post")
    val post: Int? = null,

    @field:SerializedName("likes")
    val likes: Int? = null
)

data class PostItem(

    @field:SerializedName("UPDATEDAT")
    val uPDATEDAT: String,

    @field:SerializedName("POSTID")
    val pOSTID: String,

    @field:SerializedName("USERID")
    val uSERID: String,

    @field:SerializedName("POSTIMG")
    val pOSTIMG: String? = null,

    @field:SerializedName("CREATEDAT")
    val cREATEDAT: String,

    @field:SerializedName("BATIKID")
    val bATIKID: String,

    @field:SerializedName("CAPTION")
    val cAPTION: String,

    @field:SerializedName("LIKES")
    val lIKES: Int? = null,

    @field:SerializedName("batik")
    val batik: BatikModel
)