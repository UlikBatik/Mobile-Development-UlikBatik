package com.example.ulikbatik.data.remote.request

import com.google.gson.annotations.SerializedName

data class LoginBodyRequest(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String
)
