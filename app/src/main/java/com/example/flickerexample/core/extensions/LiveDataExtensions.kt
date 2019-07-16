package com.example.flickerexample.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T> T.liveData() = MutableLiveData<T>().apply { value = this@liveData }

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer : (value : T?) -> Unit) = observe(lifecycleOwner, Observer {
    observer(it)
})

var <T> MutableLiveData<T>.post: T?
    get() = value
    set(value) {
        postValue(value)
    }