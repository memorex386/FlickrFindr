package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Description(

	@Json(name="_content")
	val content: String? = null
): Parcelable