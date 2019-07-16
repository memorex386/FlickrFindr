package com.example.flickerexample.core

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        app = this

        AndroidThreeTen.init(this);
    }

}

lateinit var app: Application
    private set

