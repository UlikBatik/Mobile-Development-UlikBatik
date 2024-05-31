package com.example.ulikbatik.data.model

import com.google.gson.annotations.SerializedName

data class UserModel(

	@field:SerializedName("PASSWORD")
	val pASSWORD: String? = null,

	@field:SerializedName("UPDATEDAT")
	val uPDATEDAT: String? = null,

	@field:SerializedName("USERID")
	val uSERID: String? = null,

	@field:SerializedName("USERNAME")
	val uSERNAME: String? = null,

	@field:SerializedName("CREATEDAT")
	val cREATEDAT: String? = null,

	@field:SerializedName("PROFILEIMG")
	val pROFILEIMG: Any? = null,

	@field:SerializedName("EMAIL")
	val eMAIL: String? = null
)
