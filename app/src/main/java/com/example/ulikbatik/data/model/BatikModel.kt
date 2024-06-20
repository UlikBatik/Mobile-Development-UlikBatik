package com.example.ulikbatik.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BatikModel(

    @field:SerializedName("BATIKNAME")
    val bATIKNAME: String,

    @field:SerializedName("BATIKDESC")
    val bATIKDESC: String,

    @field:SerializedName("BATIKLOCT")
    val bATIKLOCT: String,

    @field:SerializedName("BATIKHIST")
    val bATIKHIST: String,

    @field:SerializedName("BATIKID")
    val bATIKID: String,

    @field:SerializedName("BATIKIMG")
    val bATIKIMG: String
) : Serializable
