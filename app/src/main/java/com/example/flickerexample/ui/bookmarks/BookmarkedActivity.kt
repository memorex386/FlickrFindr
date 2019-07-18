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
import com.example.flickerexample.core.base.ResultLiveDataAction
import com.example.flickerexample.core.extensions.addStart
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

        fun checkList(items: Collection<PhotoItem>?) {
            recyclerView.isVisible = !items.isNullOrEmpty()

            none_found.isVisible = items.isNullOrEmpty()
        }

        viewModel.bookmarks.observe {
            recyclerView.adapter =
                if (it.isNullOrEmpty()) null else BookmarkResultsAdapter(it) { imageView, photoItem ->
                    viewModel.lastClicked = photoItem
                    ImageFullScreenActivity.startIntent(this, photoItem, imageView)
                }

            checkList(it)
        }

        viewModel.fetchBookmarks()

        viewModel.bookmarkChecked.observe { result ->
            if (result.second) return@observe
            (recyclerView.adapter as? BookmarkResultsAdapter)?.apply {
                removeItem { it.id == result.first.id }
                checkList(photos)
            }

        }

        lifecycle.addStart {
            viewModel.lastClicked?.also { found ->
                viewModel.checkBookmark(found)
            }
        }

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
    ResultsAdapter<BookmarkResultsAdapterHolder>(photos.toMutableList(), imageClicked) {
    override fun createViewHolder(view: View) = BookmarkResultsAdapterHolder(view)
}


class BookmarksViewModel : BaseViewModel() {

    val bookmarks = MutableLiveData<List<PhotoItem>>()

    var lastClicked: PhotoItem? = null

    val bookmarkChecked = ResultLiveDataAction<Pair<PhotoItem, Boolean>>()

    fun checkBookmark(photo: PhotoItem) = scope.launch {
        bookmarkChecked.postTrigger(Pair(photo, flickerDB.photoItemDao().getById(photo.id) != null))
    }

    fun fetchBookmarks() = scope.launch {
        bookmarks.post = flickerDB.photoItemDao().getAll()
    }

}
