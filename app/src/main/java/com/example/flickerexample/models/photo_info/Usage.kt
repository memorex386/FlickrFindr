package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Usage(

	@Json(name="canshare")
	val canshare: Int? = null,

	@Json(name="canprint")
	val canprint: Int? = null,

	@Json(name="canblog")
	val canblog: Int? = null,

	@Json(name="candownload")
	val candownload: Int? = null
): Parcelable