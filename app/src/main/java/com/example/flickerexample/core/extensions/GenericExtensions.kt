package com.example.flickerexample.core.extensions

import android.widget.EditText

var EditText.textString: String
    get() = text.toString()
    set(value) {
        setText(value)
    }