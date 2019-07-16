package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class TagItem(

	@Json(name="author")
	val author: String? = null,

	@Json(name="machine_tag")
	val machineTag: Boolean? = null,

	@Json(name="authorname")
	val authorname: String? = null,

	@Json(name="raw")
	val raw: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="_content")
	val content: String? = null
): Parcelable