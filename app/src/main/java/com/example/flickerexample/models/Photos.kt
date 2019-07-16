package com.example.flickerexample.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photos(

	@Json(name="perpage")
	val perpage: Int,

	@Json(name="total")
	val total: Int,

	@Json(name="pages")
	val pages: Int,

	@Json(name="photo")
	val photo: List<PhotoItem>,

	@Json(name="page")
	val page: Int,

	var searchTerm: String = ""
) : Parcelable