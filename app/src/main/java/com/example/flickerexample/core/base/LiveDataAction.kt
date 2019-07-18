package com.example.flickerexample.core.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

abstract class BaseLiveDataAction<T> {

    protected val trigger = MutableLiveData<T>()

    abstract fun observe(owner: LifecycleOwner, observer: (T) -> Unit)

    abstract fun trigger(input: T? = null)

    abstract fun postTrigger(input: T? = null)

}

class LiveDataAction : BaseLiveDataAction<Boolean>() {
    override fun observe(owner: LifecycleOwner, observer: (Boolean) -> Unit) = trigger.observe(owner, Observer {
        if (it != true) return@Observer
        observer(it)
        trigger.value = false
    })

    override fun postTrigger(input: Boolean?) {
        trigger.postValue(true)
    }

    override fun trigger(input: Boolean?) {
        trigger.value = true
    }
}

class ResultLiveDataAction<T> : BaseLiveDataAction<T>() {
    override fun observe(owner: LifecycleOwner, observer: (T) -> Unit) = trigger.observe(owner, Observer {
        if (it == null) return@Observer
        observer(it)
        trigger.value = null
    })

    override fun postTrigger(input: T?) {
        trigger.postValue(input)
    }

    override fun trigger(input: T?) {
        trigger.value = input
    }
}

