package com.mufiid.assetmanagement.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asset(

	@field:SerializedName("no_rangka")
	val noRangka: String? = null,

	@field:SerializedName("date_oil")
	val dateOil: String? = null,

	@field:SerializedName("no_mesin")
	val noMesin: String? = null,

	@field:SerializedName("owner_name")
	val ownerName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("plat_mobil")
	val platMobil: String? = null,

	@field:SerializedName("merk_mobil")
	val merkMobil: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("date_expired_oil")
	val dateExpiredOil: String? = null,

	@field:SerializedName("status")
	val status: String? = null
): Parcelable
