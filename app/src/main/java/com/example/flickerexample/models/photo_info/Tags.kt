package com.example.flickerexample.models.photo_info

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Tags(

	@Json(name="tag")
	val tag: List<TagItem?>? = null
): Parcelable