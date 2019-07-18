package com.example.flickerexample.ui.image


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.flickerexample.R
import com.example.flickerexample.core.app
import com.example.flickerexample.core.base.BaseViewModel
import com.example.flickerexample.core.base.BaseViewModelActivity
import com.example.flickerexample.core.base.LiveDataAction
import com.example.flickerexample.core.extensions.animateFade
import com.example.flickerexample.core.extensions.liveDataNotNull
import com.example.flickerexample.core.extensions.load
import com.example.flickerexample.core.extensions.post
import com.example.flickerexample.models.photo_info.PhotoInfoResponse
import com.example.flickerexample.models.photos.PhotoItem
import com.example.flickerexample.models.photos.getPhotoUrl
import com.example.flickerexample.network.PhotoRepository
import com.example.flickerexample.room.flickerDB
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_image_full_screen.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import java.io.File
import java.io.FileOutputStream


class ImageFullScreenActivity : BaseViewModelActivity<ImageFullScreenViewModel>(ImageFullScreenViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen)

        viewModel.setFromIntent(intent)

        supportPostponeEnterTransition()

        viewModel.photo.observeNotNull {

            val loaded: ((Boolean) -> Unit) = { success ->
                supportStartPostponedEnterTransition()
                image_flickr_title.text = it.title
            }

            full_screen_image.apply {
                when ((it.dateBookmarked != null)) {
                    true -> load(File(app.filesDir, it.id), loaded)
                    false -> load(it.getPhotoUrl(), loaded)
                }
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

        viewModel.hasBookmark.observeNotNull {
            bookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    if (it) R.drawable.ic_bookmark_black_24dp else R.drawable.ic_bookmark_border_black_24dp
                )
            )
        }

        bookmark.setOnClickListener {
            val bitmap = (full_screen_image.drawable as? BitmapDrawable)?.bitmap

            if (bitmap == null) {
                Snackbar.make(image_root, getString(R.string.error_encountered), Snackbar.LENGTH_SHORT).show()
            } else {
                loading.animateFade(true)
                viewModel.bookmarkClicked(bitmap)
            }
        }

        viewModel.checkBookmark()

        viewModel.bookmarkChangeComplete.observe {
            loading.animateFade(false)
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

    val hasBookmark = false.liveDataNotNull()

    val bookmarkChangeComplete = LiveDataAction()

    fun checkBookmark() = scope.launch {
        photo.value?.also {
            hasBookmark.post = flickerDB.photoItemDao().getById(it.id) != null
        }
    }

    fun setFromIntent(intent: Intent) {
        photo.value = intent.getParcelableExtra(ImageFullScreenActivity.PHOTO)
        scope.launch {
            photo.value?.also {
                photoInfo.post = PhotoRepository.getPhotoInfo(it).resultElseNull
            }
        }
    }

    fun bookmarkClicked(bitmap: Bitmap? = null) {
        val photo = photo.value ?: return
        scope.launch {
            flickerDB.photoItemDao().apply {
                val file = File(app.filesDir, photo.id)
                if (!hasBookmark.value) {
                    photo.dateBookmarked = Instant.now().toEpochMilli()
                    insert(photo)
                    bitmap?.saveToFile(file)
                } else {
                    photo.dateBookmarked = null
                    delete(photo)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }
            hasBookmark.post = !hasBookmark.value
            bookmarkChangeComplete.postTrigger()
        }
    }

}

fun Bitmap.saveToFile(file: File) {
    FileOutputStream(file).use {
        compress(Bitmap.CompressFormat.PNG, 90, it)
        it.close()
    }
}

