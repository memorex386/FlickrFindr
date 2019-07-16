package com.example.flickerexample.core.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class LiveDataAction {

    private val trigger = MutableLiveData<Boolean>()

    fun observe(owner: LifecycleOwner, observer: () -> Unit) = trigger.observe(owner, Observer {
        if (it != true) return@Observer
        observer()
        trigger.value = false
    })

    fun trigger() {
        trigger.value = true
    }

    fun postTrigger() {
        trigger.postValue(true)
    }

}