package com.example.ulikbatik.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean,

	@field:SerializedName("token")
	val token: String? = null
)

data class Data(

	@field:SerializedName("PASSWORD")
	val password: String? = null,

	@field:SerializedName("UPDATEDAT")
	val updateAt: String? = null,

	@field:SerializedName("USERID")
	val userId: String? = null,

	@field:SerializedName("USERNAME")
	val username: String? = null,

	@field:SerializedName("CREATEDAT")
	val createdAt: String? = null,

	@field:SerializedName("PROFILEIMG")
	val profileImage: Any? = null,

	@field:SerializedName("EMAIL")
	val email: String? = null
)
