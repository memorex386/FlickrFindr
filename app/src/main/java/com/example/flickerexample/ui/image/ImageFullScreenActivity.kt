package com.example.flickerexample.ui.image


import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.MutableLiveData
import com.example.flickerexample.R
import com.example.flickerexample.core.base.BaseViewModel
import com.example.flickerexample.core.base.BaseViewModelActivity
import com.example.flickerexample.core.extensions.load
import com.example.flickerexample.models.PhotoItem
import com.example.flickerexample.models.getPhotoUrl
import kotlinx.android.synthetic.main.activity_image_full_screen.*

class ImageFullScreenActivity : BaseViewModelActivity<ImageFullScreenViewModel>(ImageFullScreenViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen)

        supportPostponeEnterTransition()

        viewModel.photo.observeNotNulls {
            full_screen_image.load(it.getPhotoUrl()) {
                supportStartPostponedEnterTransition()
            }
        }

        viewModel.setFromIntent(intent)
    }

    companion object {
        val PHOTO = "PHOTO"

        fun startIntent(activity: Activity, photoItem: PhotoItem, imageView: ImageView) {
            val intent = Intent(activity, ImageFullScreenActivity::class.java).apply {
                putExtra(PHOTO, photoItem)
            }
            val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView, imageView.transitionName)
            } else {
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView, "result_image")
            }
            activity.startActivity(intent, options.toBundle())
        }
    }
}

class ImageFullScreenViewModel : BaseViewModel(){

    val photo = MutableLiveData<PhotoItem>()

    fun setFromIntent(intent: Intent) {
        photo.value = intent.getParcelableExtra(ImageFullScreenActivity.PHOTO)
    }

}

