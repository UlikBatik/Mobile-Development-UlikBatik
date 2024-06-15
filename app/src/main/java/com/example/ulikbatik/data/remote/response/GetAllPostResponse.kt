package com.example.ulikbatik.data.remote.response

import com.example.ulikbatik.data.model.PostModel
import com.google.gson.annotations.SerializedName

data class GetAllPostResponse(

	@field:SerializedName("totalData")
	val totalData: Int? = null,

	@field:SerializedName("data")
	val data: List<PostModel>,

	@field:SerializedName("totalPage")
	val totalPage: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("currentPage")
	val currentPage: Int? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
