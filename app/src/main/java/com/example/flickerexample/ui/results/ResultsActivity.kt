package com.example.flickerexample.ui.results


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.flickerexample.R
import com.example.flickerexample.core.base.BaseViewModel
import com.example.flickerexample.core.base.BaseViewModelActivity
import com.example.flickerexample.models.PhotoItem
import com.example.flickerexample.models.Photos
import com.example.flickerexample.models.getPhotoUrl
import com.example.flickerexample.ui.image.ImageFullScreenActivity
import kotlinx.android.synthetic.main.activity_results.*


class ResultsActivity : BaseViewModelActivity<ResultsViewModel>(ResultsViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.photosResultsLiveData.observe {
            it ?: return@observe
            recyclerView.adapter = ResultsAdapter(it) { imageView, photoItem ->
                ImageFullScreenActivity.startIntent(this, photoItem, imageView)
            }
        }

        viewModel.setFromIntent(intent)


    }

    companion object {
        val PHOTOS = "PHOTOS"

        fun getIntent(context: Context, photos: Photos) = Intent(context, ResultsActivity::class.java).apply {
            putExtra(PHOTOS, photos)
        }
    }
}

class ResultsAdapterHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    val image = itemView.findViewById<ImageView>(R.id.image_flickr)
    val imageTitle = itemView.findViewById<TextView>(R.id.image_flickr_title)

    fun bind(photo : PhotoItem){

        Glide.with(image)
            .load(photo.getPhotoUrl())
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
            .into(image)

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

class ResultsViewModel : BaseViewModel(){

    val photosResultsLiveData = MutableLiveData<Photos>()

    fun setFromIntent(intent : Intent?){
        val photos = intent?.getParcelableExtra<Photos>(ResultsActivity.PHOTOS)
        photosResultsLiveData.value = photos
    }

}
