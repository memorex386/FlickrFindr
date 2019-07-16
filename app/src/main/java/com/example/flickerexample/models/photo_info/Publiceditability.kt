package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Publiceditability(

	@Json(name="cancomment")
	val cancomment: Int? = null,

	@Json(name="canaddmeta")
	val canaddmeta: Int? = null
): Parcelable