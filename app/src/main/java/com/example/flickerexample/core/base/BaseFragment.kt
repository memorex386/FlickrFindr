package com.example.flickerexample.core.base


import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

abstract class BaseFragment : Fragment() {

}

abstract class BaseViewModelFragment<T : BaseViewModel>(private val _viewModelType: Class<T>) : BaseFragment() {

    val viewModel: T by lazy { ViewModelProviders.of(this).get(_viewModelType) }

    fun <T> BaseLiveDataAction<T>.observe(observer: (T) -> Unit) = observe(this@BaseViewModelFragment, observer)

    fun <T> LiveData<T>.singleObserve(observer: (T?) -> Unit) {
        removeObservers(this@BaseViewModelFragment)
        observe(this@BaseViewModelFragment, Observer(observer))
    }

    fun <T> LiveData<T>.singleObserveNotNull(observer: (T) -> Unit) {
        removeObservers(this@BaseViewModelFragment)
        observe(this@BaseViewModelFragment, Observer {
            if (it != null) observer(it)
        })
    }

    fun <T> LiveData<T>.observe(observer: (T?) -> Unit) {
        observe(this@BaseViewModelFragment, Observer(observer))
    }

    fun <T> LiveData<T>.observeNotNull(observer: (T) -> Unit) {
        observe(this@BaseViewModelFragment, Observer {
            if (it != null) observer(it)
        })
    }
}