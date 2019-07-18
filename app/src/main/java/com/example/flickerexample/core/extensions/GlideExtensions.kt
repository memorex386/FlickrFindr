package com.example.flickerexample.core.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.flickerexample.R
import java.io.File


private fun ImageView.intloadAnimate(
    animate: Boolean,
    load: RequestManager.() -> RequestBuilder<Drawable>,
    onLoadingFinished: (success: Boolean) -> Unit = {}
) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished(false)
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished(true)
            return false
        }

    }

    val requestOptions = RequestOptions.placeholderOf(R.drawable.ic_photo_size_select_actual_black_24dp)
    // .dontTransform()

    Glide.with(this)
        .load()
        .apply(requestOptions)
        .listener(listener).apply {
            if (animate) transition(DrawableTransitionOptions.withCrossFade())
        }
        .error(R.drawable.ic_photo_size_select_actual_black_24dp)
        .into(this)
}

fun ImageView.loadAnimate(file: File, animate: Boolean, onLoadingFinished: (success: Boolean) -> Unit = {}) =
    intloadAnimate(animate, { load(file) }, onLoadingFinished)


fun ImageView.loadAnimate(url: String, animate: Boolean, onLoadingFinished: (success: Boolean) -> Unit = {}) =
    intloadAnimate(animate, { load(url) }, onLoadingFinished)

fun ImageView.load(url: String, onLoadingFinished: (success: Boolean) -> Unit) =
    loadAnimate(url, false, onLoadingFinished)


fun ImageView.load(file: File, onLoadingFinished: (success: Boolean) -> Unit) =
    loadAnimate(file, false, onLoadingFinished)
