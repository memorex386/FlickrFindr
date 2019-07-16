package com.example.flickerexample.core.extensions

import android.content.Context
import com.example.flickerexample.core.app

object SharedPref {

    val pref by lazy { app.getSharedPreferences(app.packageName, Context.MODE_PRIVATE) }

}