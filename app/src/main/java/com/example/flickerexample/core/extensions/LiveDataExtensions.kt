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

open class MutableLiveDataNotNull<T>(_value: T) : MutableLiveData<T>() {

    init {
        value = _value
    }

    override fun getValue(): T {
        return super.getValue()!!
    }

}

fun <T> T.liveDataNotNull() = MutableLiveDataNotNull<T>(this)
