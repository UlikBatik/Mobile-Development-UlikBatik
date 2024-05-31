package com.example.ulikbatik.data.model

import com.google.gson.annotations.SerializedName

data class BatikModel(

	@field:SerializedName("BATIKNAME")
	val bATIKNAME: String? = null,

	@field:SerializedName("BATIKDESC")
	val bATIKDESC: String? = null,

	@field:SerializedName("BATIKID")
	val bATIKID: String? = null,

	@field:SerializedName("BATIKIMG")
	val bATIKIMG: String? = null
)
