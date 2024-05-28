package com.example.ulikbatik.data.remote.request

import com.google.gson.annotations.SerializedName

data class RegisterBodyRequest(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("confirmpassword")
	val confirmPassword: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
