package com.example.flickerexample.core.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

var EditText.textString: String
    get() = text.toString()
    set(value) {
        setText(value)
    }

fun EditText.onActionSearch(triggered: (String) -> Unit) {
    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard()
            triggered(textString)
            true
        } else false
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun View.hideKeyboard() = context?.hideKeyboard(this)

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun View.animateFade(show: Boolean) {
    AlphaAnimation(if (show) 0f else 1f, if (show) 1f else 0f).apply {
        interpolator = DecelerateInterpolator()
        duration = 300L
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                isVisible = true
            }

            override fun onAnimationEnd(animation: Animation) {
                isVisible = show
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        startAnimation(this)
    }

}