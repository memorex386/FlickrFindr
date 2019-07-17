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
import com.example.flickerexample.models.photos.*
import com.example.flickerexample.network.PhotoRepository
import com.example.flickerexample.ui.image.ImageFullScreenActivity
import com.example.flickerexample.ui.search.BaseSearchActivity
import com.example.flickerexample.ui.search.SearchViewModel
import com.google.android.material.snackbar.Snackbar
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
            PhotoRepository.lastResult.value?.photos?.takeIf { it.page > 1 && viewModel.isLoading.value != true }
                ?.also {
                    PhotoRepository.currentPage--
                    trySearch()
                }
        }

        page_right.setOnClickListener {
            PhotoRepository.lastResult.value?.photos?.takeIf { it.page < it.pages && viewModel.isLoading.value != true }
                ?.also {
                    PhotoRepository.currentPage++
                    trySearch()
                }
        }

        PhotoRepository.lastResult.observe {
            if (it?.isSuccessful != true || it.photos == null) {
                Snackbar.make(rootView(), it?.message ?: "Unable to process", Snackbar.LENGTH_SHORT)
                    .show();
                return@observe
            }

            recyclerView.adapter = ResultsAdapter(it.photos) { imageView, photoItem ->
                ImageFullScreenActivity.startIntent(this, photoItem, imageView)
            }

            search_text.text = it.searchTerm

            page_title.text = Html.fromHtml("Page <b>${it.photos.page}</b> of ${it.photos.pages}")
        }

        viewModel.photosFetchedAction.observe {
            showLoading(false)
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

class ResultsAdapterHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    val image = itemView.findViewById<ImageView>(R.id.image_flickr)
    val imageTitle = itemView.findViewById<TextView>(R.id.image_flickr_title)

    fun bind(photo : PhotoItem){

        image.load(photo.getPhotoUrl())

        imageTitle.text = photo.title
    }

}

class ResultsAdapter(val photos: Photos, val imageClicked: (ImageView, PhotoItem) -> Unit) :
    RecyclerView.Adapter<ResultsAdapterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_result_view_holder, parent, false)
        return ResultsAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsAdapterHolder, position: Int) {
        val photo = photos.photo[position]
        holder.bind(photo)
        holder.itemView.setOnClickListener {
            imageClicked(holder.image, photo)
        }

    }

    override fun getItemCount() = photos.photo.size

}

class ResultsViewModel : SearchViewModel() {

}
