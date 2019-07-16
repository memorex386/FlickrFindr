package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Dates(

	@Json(name="taken")
	val taken: String? = null,

	@Json(name="takengranularity")
	val takengranularity: String? = null,

	@Json(name="lastupdate")
	val lastupdate: String? = null,

	@Json(name="takenunknown")
	val takenunknown: String? = null,

	@Json(name="posted")
	val posted: String? = null
): Parcelable