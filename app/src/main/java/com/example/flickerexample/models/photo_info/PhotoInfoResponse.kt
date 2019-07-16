package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class PhotoInfoResponse(

	@Json(name = "stat")
	val stat: String? = null,

	@Json(name = "code")
	val code: String? = null,

	@Json(name = "message")
	val message: String? = null,

	@Json(name="photo")
	val photo: Photo? = null
): Parcelable