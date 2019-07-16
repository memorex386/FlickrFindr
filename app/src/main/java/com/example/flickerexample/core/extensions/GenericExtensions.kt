package com.example.flickerexample.core.extensions

import android.view.inputmethod.EditorInfo
import android.widget.EditText

var EditText.textString: String
    get() = text.toString()
    set(value) {
        setText(value)
    }

fun EditText.onActionSearch(triggered: (String) -> Unit) {
    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            triggered(textString)
            true
        } else false
    }
}
