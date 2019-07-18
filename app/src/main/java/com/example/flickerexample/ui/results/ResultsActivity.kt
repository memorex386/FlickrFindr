package com.example.flickerexample.ui.results


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.flickerexample.R
import com.example.flickerexample.core.extensions.load
import com.example.flickerexample.models.photos.PhotoItem
import com.example.flickerexample.models.photos.PhotoSearchResults
import com.example.flickerexample.models.photos.getPhotoUrl
import com.example.flickerexample.models.photos.isSuccessful
import com.example.flickerexample.network.PhotoRepository
import com.example.flickerexample.ui.image.ImageFullScreenActivity
import com.example.flickerexample.ui.search.BaseSearchActivity
import com.example.flickerexample.ui.search.SearchViewModel
import kotlinx.android.synthetic.main.activity_results.*
import kotlinx.android.synthetic.main.loading.*


class ResultsActivity : BaseSearchActivity<ResultsViewModel>(ResultsViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        search_text.onActionSearch(::trySearch)

        page_left.setOnClickListener {
            viewModel.changePage(-1) {
                trySearch()
            }
        }

        page_right.setOnClickListener {
            viewModel.changePage(1) {
                trySearch()
            }
        }


        fun onSuccessResult(photos: PhotoSearchResults?) {
            if (photos?.isSuccessful != true || photos.photos?.photo == null) {
                snackBar(photos?.message ?: "Unable to process")
                return
            }

            recyclerView.adapter = SearchResultsAdapter(photos.photos.photo) { imageView, photoItem ->
                ImageFullScreenActivity.startIntent(this, photoItem, imageView)
            }

            search_text.text = photos.searchTerm

            page_title.text = Html.fromHtml("Page <b>${photos.photos.page}</b> of ${photos.photos.pages}")
        }

        onSuccessResult(PhotoRepository.lastResult.value)

        viewModel.photosFetchedAction.observe {
            showLoading(false)

            it

                .ifError {
                    snackBar(it.localizedMessage)
                }

                .ifSuccess(::onSuccessResult)
        }

    }

    override fun rootView() = results_root

    override fun loading() = loading

    companion object {
        val PHOTOS = "PHOTOS"

        fun getIntent(context: Context, photos: PhotoSearchResults) =
            Intent(context, ResultsActivity::class.java).apply {
                putExtra(PHOTOS, photos)
            }

        fun startIntent(activity: Activity, photos: PhotoSearchResults, view: View) {
            val intent = getIntent(activity, photos)
            val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, view.transitionName)
            } else {
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, "search_query")
            }
            activity.startActivity(intent, options.toBundle())
        }
    }
}


abstract class ResultsAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val image = itemView.findViewById<ImageView>(R.id.image_flickr)
    val imageTitle = itemView.findViewById<TextView>(R.id.image_flickr_title)

    abstract fun bindPhotoPath(photo: PhotoItem)

    fun bind(photo : PhotoItem){

        bindPhotoPath(photo)


        imageTitle.text = photo.title
    }

}


class SearchResultsAdapterHolder(itemView: View) : ResultsAdapterHolder(itemView) {
    override fun bindPhotoPath(photo: PhotoItem) {
        image.load(photo.getPhotoUrl())
    }
}


class SearchResultsAdapter(photos: List<PhotoItem>, imageClicked: (ImageView, PhotoItem) -> Unit) :
    ResultsAdapter<SearchResultsAdapterHolder>(photos, imageClicked) {
    override fun createViewHolder(view: View) = SearchResultsAdapterHolder(view)
}


abstract class ResultsAdapter<T : ResultsAdapterHolder>(
    val photos: List<PhotoItem>,
    val imageClicked: (ImageView, PhotoItem) -> Unit
) :
    RecyclerView.Adapter<ResultsAdapterHolder>() {

    abstract fun createViewHolder(view: View): T

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_result_view_holder, parent, false)
        return createViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsAdapterHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
        holder.itemView.setOnClickListener {
            imageClicked(holder.image, photo)
        }

    }

    override fun getItemCount() = photos.size

}

class ResultsViewModel : SearchViewModel() {

    fun changePage(pageAdd: Int, pageAllowed: () -> Unit) {

        fun complete() {
            PhotoRepository.currentPage += pageAdd
            pageAllowed()
        }

        if (pageAdd == 0) return
        else if (pageAdd < 0) {
            PhotoRepository.lastResult.value?.photos?.takeIf { it.page > 1 && isLoading.value != true }
                ?.also {
                    complete()
                }
        } else {
            PhotoRepository.lastResult.value?.photos?.takeIf { it.page < it.pages && isLoading.value != true }
                ?.also {
                    complete()
                }
        }
    }

}
