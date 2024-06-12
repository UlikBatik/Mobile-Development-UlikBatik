package com.example.ulikbatik.data.remote.response

import com.google.gson.annotations.SerializedName

data class ScrapperResponse(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("link")
	val link: String,

	@field:SerializedName("title")
	val title: String
)
