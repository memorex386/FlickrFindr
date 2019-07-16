package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Visibility(

	@Json(name="ispublic")
	val ispublic: Int? = null,

	@Json(name="isfriend")
	val isfriend: Int? = null,

	@Json(name="isfamily")
	val isfamily: Int? = null
): Parcelable