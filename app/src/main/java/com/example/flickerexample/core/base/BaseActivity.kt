package com.example.flickerexample.core.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

abstract class BaseActivity : AppCompatActivity(), LifecycleOwner{

}

abstract class BaseViewModelActivity<T : BaseViewModel>(private val _viewModelType : Class<T>) : BaseActivity(){

    val viewModel : T by lazy { ViewModelProviders.of(this).get(_viewModelType) }

    fun LiveDataAction.observe(observer: () -> Unit) = observe(this@BaseViewModelActivity, observer)

    fun <T> LiveData<T>.singleObserve(observer: (T?) -> Unit) {
        removeObservers(this@BaseViewModelActivity)
        observe(this@BaseViewModelActivity, Observer(observer))
    }

    fun <T> LiveData<T>.singleObserveNotNull(observer: (T) -> Unit) {
        removeObservers(this@BaseViewModelActivity)
        observe(this@BaseViewModelActivity, Observer {
            if (it != null) observer(it)
        })
    }

    fun <T> LiveData<T>.observe(observer: (T?) -> Unit) {
        observe(this@BaseViewModelActivity, Observer(observer))
    }

    fun <T> LiveData<T>.observeNotNull(observer: (T) -> Unit) {
        observe(this@BaseViewModelActivity, Observer {
            if (it != null) observer(it)
        })
    }
}