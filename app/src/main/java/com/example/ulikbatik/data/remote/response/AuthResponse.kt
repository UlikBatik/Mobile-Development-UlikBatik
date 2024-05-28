package com.example.ulikbatik.data.remote.response

data class AuthResponse(
	val data: String,
	val message: String,
	val status: Boolean,
	val token: String
)
