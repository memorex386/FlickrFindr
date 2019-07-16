package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Owner(

	@Json(name="nsid")
	val nsid: String? = null,

	@Json(name="iconfarm")
	val iconfarm: Int? = null,

	@Json(name="path_alias")
	val pathAlias: String? = null,

	@Json(name="iconserver")
	val iconserver: String? = null,

	@Json(name="location")
	val location: String? = null,

	@Json(name="username")
	val username: String? = null,

	@Json(name="realname")
	val realname: String? = null
): Parcelable