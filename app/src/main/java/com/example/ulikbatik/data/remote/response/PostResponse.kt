package com.example.ulikbatik.data.remote.response

import com.example.ulikbatik.data.model.BatikModel
import com.example.ulikbatik.data.model.UserModel
import com.google.gson.annotations.SerializedName


data class PostResponse(

    @field:SerializedName("batik")
    val batik: BatikModel,

    @field:SerializedName("UPDATEDAT")
    val uPDATEDAT: String,

    @field:SerializedName("POSTID")
    val pOSTID: String,

    @field:SerializedName("USERID")
    val uSERID: String,

    @field:SerializedName("POSTIMG")
    val pOSTIMG: String,

    @field:SerializedName("CREATEDAT")
    val cREATEDAT: String,

    @field:SerializedName("BATIKID")
    val bATIKID: String,

    @field:SerializedName("CAPTION")
    val cAPTION: String,

    @field:SerializedName("LIKES")
    val lIKES: Int? = null,

    @field:SerializedName("user")
    val user: UserModel
)