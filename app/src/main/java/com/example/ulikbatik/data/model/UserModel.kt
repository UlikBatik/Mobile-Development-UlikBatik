package com.example.ulikbatik.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(

    @field:SerializedName("PASSWORD")
    val pASSWORD: String,

    @field:SerializedName("UPDATEDAT")
    val uPDATEDAT: String,

    @field:SerializedName("USERID")
    val uSERID: String,

    @field:SerializedName("USERNAME")
    val uSERNAME: String,

    @field:SerializedName("CREATEDAT")
    val cREATEDAT: String,

    @field:SerializedName("PROFILEIMG")
    val pROFILEIMG: String? = null,

    @field:SerializedName("EMAIL")
    val eMAIL: String
)
