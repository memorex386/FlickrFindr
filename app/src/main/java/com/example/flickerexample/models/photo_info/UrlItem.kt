package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class UrlItem(

	@Json(name="type")
	val type: String? = null,

	@Json(name="_content")
	val content: String? = null
): Parcelable