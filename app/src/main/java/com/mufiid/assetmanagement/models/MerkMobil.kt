package com.mufiid.assetmanagement.models

import com.google.gson.annotations.SerializedName

data class MerkMobil(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
