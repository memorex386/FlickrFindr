package com.example.flickerexample.ui.bookmarks


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.flickerexample.R
import com.example.flickerexample.core.app
import com.example.flickerexample.core.base.BaseViewModel
import com.example.flickerexample.core.base.BaseViewModelActivity
import com.example.flickerexample.core.extensions.post
import com.example.flickerexample.models.photos.PhotoItem
import com.example.flickerexample.room.flickerDB
import com.example.flickerexample.ui.image.ImageFullScreenActivity
import com.example.flickerexample.ui.results.ResultsAdapter
import com.example.flickerexample.ui.results.ResultsAdapterHolder
import kotlinx.android.synthetic.main.activity_bookmarks.*
import kotlinx.android.synthetic.main.activity_results.recyclerView
import kotlinx.android.synthetic.main.activity_results.toolbar
import kotlinx.coroutines.launch
import java.io.File


class BookmarkedActivity : BaseViewModelActivity<BookmarksViewModel>(BookmarksViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel.bookmarks.observe {
            recyclerView.adapter =
                if (it.isNullOrEmpty()) null else BookmarkResultsAdapter(it) { imageView, photoItem ->
                    ImageFullScreenActivity.startIntent(this, photoItem, imageView)
                }

            recyclerView.isVisible = !it.isNullOrEmpty()

            none_found.isVisible = it.isNullOrEmpty()
        }

        viewModel.fetchBookmarks()

    }


    companion object {

        fun getIntent(context: Context) = Intent(context, BookmarkedActivity::class.java)
    }
}


class BookmarkResultsAdapterHolder(itemView: View) : ResultsAdapterHolder(itemView) {
    override fun bindPhotoPath(photo: PhotoItem) {
        Glide.with(image)
            .load(File(app.filesDir, photo.id))
            .into(image);
    }
}


class BookmarkResultsAdapter(photos: List<PhotoItem>, imageClicked: (ImageView, PhotoItem) -> Unit) :
    ResultsAdapter<BookmarkResultsAdapterHolder>(photos, imageClicked) {
    override fun createViewHolder(view: View) = BookmarkResultsAdapterHolder(view)
}


class BookmarksViewModel : BaseViewModel() {

    val bookmarks = MutableLiveData<List<PhotoItem>>()


    fun fetchBookmarks() = scope.launch {
        bookmarks.post = flickerDB.photoItemDao().getAll()
    }

}
