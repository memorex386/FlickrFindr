package com.example.flickerexample.core.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProviders

abstract class BaseActivity : AppCompatActivity(), LifecycleOwner{

}

abstract class BaseViewModelActivity<T : BaseViewModel>(private val _viewModelType : Class<T>) : BaseActivity(){
    val viewModel : T by lazy { ViewModelProviders.of(this).get(_viewModelType) }
}