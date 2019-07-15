package com.example.flickerexample.ui.results


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
import com.example.flickerexample.R
import com.example.flickerexample.core.base.BaseViewModel
import com.example.flickerexample.core.base.BaseViewModelActivity
import com.example.flickerexample.core.extensions.observe
import com.example.flickerexample.models.PhotoItem
import com.example.flickerexample.models.Photos
import com.example.flickerexample.models.getPhotoUrl
import kotlinx.android.synthetic.main.activity_results.*


class ResultsActivity : BaseViewModelActivity<ResultsViewModel>(ResultsViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.photosResultsLiveData.observe(this){
            it ?: return@observe
            recyclerView.adapter = ResultsAdapter(it)
        }

        viewModel.setFromIntent(intent)


    }

    companion object {
        val PHOTOS = "PHOTOS"
    }
}

class ResultsAdapterHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    val image = itemView.findViewById<ImageView>(R.id.image_flickr)
    val imageTitle = itemView.findViewById<TextView>(R.id.image_flickr_title)

    fun bind(photo : PhotoItem){
        Glide.with(image).load(photo.getPhotoUrl()).into(image)
        imageTitle.text = photo.title
    }

}

class ResultsAdapter(val photos : Photos) : RecyclerView.Adapter<ResultsAdapterHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_result_view_holder, parent, false)
        return ResultsAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsAdapterHolder, position: Int) {
        val photo = photos.photo[position]
        holder.bind(photo)

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
