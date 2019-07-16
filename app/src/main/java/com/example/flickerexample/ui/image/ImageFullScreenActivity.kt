package com.example.flickerexample.ui.image


import android.app.Activity
import android.content.Context
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
import com.example.flickerexample.core.extensions.post
import com.example.flickerexample.models.photo_info.PhotoInfoResponse
import com.example.flickerexample.models.photos.PhotoItem
import com.example.flickerexample.models.photos.getPhotoUrl
import com.example.flickerexample.network.PhotoRepository
import kotlinx.android.synthetic.main.activity_image_full_screen.*
import kotlinx.coroutines.launch

class ImageFullScreenActivity : BaseViewModelActivity<ImageFullScreenViewModel>(ImageFullScreenViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen)

        viewModel.setFromIntent(intent)

        supportPostponeEnterTransition()

        viewModel.photo.observeNotNull {
            full_screen_image.load(it.getPhotoUrl()) {
                supportStartPostponedEnterTransition()
                image_flickr_title.text = it.title
            }
        }

        viewModel.photoInfo.observeNotNull {
            it?.photo?.also {
                author.text = it.owner?.username
            }
        }

        close.setOnClickListener {
            onBackPressed()
        }

    }

    companion object {
        val PHOTO = "PHOTO"

        fun getIntent(context: Context, photoItem: PhotoItem) =
            Intent(context, ImageFullScreenActivity::class.java).apply {
                putExtra(PHOTO, photoItem)
            }

        fun startIntent(activity: Activity, photoItem: PhotoItem, imageView: ImageView) {
            val intent = getIntent(activity, photoItem)
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

    val photoInfo = MutableLiveData<PhotoInfoResponse>()

    fun setFromIntent(intent: Intent) {
        photo.value = intent.getParcelableExtra(ImageFullScreenActivity.PHOTO)
        scope.launch {
            photo.value?.also {
                photoInfo.post = PhotoRepository.getPhotoInfo(it)
            }
        }
    }

}

