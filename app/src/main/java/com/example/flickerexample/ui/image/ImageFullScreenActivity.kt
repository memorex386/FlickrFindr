package com.example.flickerexample.ui.image


import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.flickerexample.R
import com.example.flickerexample.core.base.BaseViewModel
import com.example.flickerexample.core.base.BaseViewModelActivity
import com.example.flickerexample.core.extensions.liveData
import com.example.flickerexample.models.Photos

class ImageFullScreenActivity : BaseViewModelActivity<ImageFullScreenViewModel>(ImageFullScreenViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen)

    }
}

class ImageFullScreenViewModel : BaseViewModel(){

    val searchString = "dogs".liveData()

    val photosResultsLiveData = MutableLiveData<Photos>()



}

