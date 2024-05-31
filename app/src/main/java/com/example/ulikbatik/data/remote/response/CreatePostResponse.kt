package com.example.ulikbatik.data.remote.response

import com.google.gson.annotations.SerializedName

data class CreatePostResponse(

	@field:SerializedName("batik")
	val batik: Batik? = null,

	@field:SerializedName("UPDATEDAT")
	val uPDATEDAT: String? = null,

	@field:SerializedName("POSTID")
	val pOSTID: String? = null,

	@field:SerializedName("USERID")
	val uSERID: String? = null,

	@field:SerializedName("POSTIMG")
	val pOSTIMG: String? = null,

	@field:SerializedName("CREATEDAT")
	val cREATEDAT: String? = null,

	@field:SerializedName("BATIKID")
	val bATIKID: String? = null,

	@field:SerializedName("CAPTION")
	val cAPTION: String? = null,

	@field:SerializedName("LIKES")
	val lIKES: Int? = null,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

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

data class Batik(

	@field:SerializedName("BATIKNAME")
	val bATIKNAME: String? = null,

	@field:SerializedName("BATIKDESC")
	val bATIKDESC: String? = null,

	@field:SerializedName("BATIKID")
	val bATIKID: String? = null,

	@field:SerializedName("BATIKIMG")
	val bATIKIMG: String? = null
)
