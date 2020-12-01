package com.mufiid.assetmanagement.models

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("id")
	var id: String? = null,

	@field:SerializedName("email")
	var email: String? = null,

	@field:SerializedName("username")
    var username: String? = null,

    @field:SerializedName("token")
    var token: String? = null,

	@field:SerializedName("status")
	var status: Any? = null
)
