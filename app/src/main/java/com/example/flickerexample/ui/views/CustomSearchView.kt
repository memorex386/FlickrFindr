package com.example.flickerexample.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.example.flickerexample.R
import com.example.flickerexample.core.extensions.onActionSearch
import com.example.flickerexample.core.extensions.textString
import kotlinx.android.synthetic.main.custom_search_view.view.*

class CustomSearchView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet,
    first: Int = -1
) : FrameLayout(context, attributeSet, first) {

    init {
        View.inflate(context, R.layout.custom_search_view, this)

        appCompatImageView.setOnClickListener {
            appCompatEditText.textString = ""
        }

    }

    var text: String
        set(value) {
            appCompatEditText.setText(value)
        }
        get() = appCompatEditText.textString

    fun onActionSearch(onActionSearch: (String) -> Unit) = appCompatEditText.onActionSearch(onActionSearch)

}