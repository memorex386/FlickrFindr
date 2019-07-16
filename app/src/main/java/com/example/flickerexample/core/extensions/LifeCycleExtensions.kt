package com.example.flickerexample.core.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

fun Lifecycle.addStart(observe: () -> Unit) = addObserver(object : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun observe() = observe()
})

fun Lifecycle.addStop(observe: () -> Unit) = addObserver(object : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun observe() = observe()
})

fun Lifecycle.addResume(observe: () -> Unit) = addObserver(object : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun observe() = observe()
})