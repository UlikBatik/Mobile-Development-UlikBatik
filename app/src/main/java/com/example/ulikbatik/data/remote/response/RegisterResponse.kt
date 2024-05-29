package com.example.ulikbatik.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class Data(

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
