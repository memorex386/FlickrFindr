package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Urls(

	@Json(name="url")
	val url: List<UrlItem?>? = null
): Parcelable