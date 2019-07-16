package com.example.flickerexample.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.flickerexample.R
import com.example.flickerexample.core.base.BaseViewModel
import com.example.flickerexample.core.base.BaseViewModelActivity
import com.example.flickerexample.models.PhotoSearchResults
import com.example.flickerexample.models.SearchQuery
import com.example.flickerexample.models.isSuccessful
import com.example.flickerexample.network.PhotoRepository
import com.example.flickerexample.room.flickerDB
import com.example.flickerexample.ui.results.ResultsActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.launch


class SearchActivity : BaseViewModelActivity<SearchViewModel>(SearchViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel.init()

        search_button_now.setOnClickListener {
            val searchString = edit_query.text
            if (searchString.isNullOrBlank()) {
                return@setOnClickListener
            }

            viewModel.fetchPhotos(searchString)

        }

        edit_query.onActionSearch(::fetchPhotos)

        viewModel.photosResultsLiveData.observe {
            if (it?.isSuccessful != true || it.photos == null) {
                Snackbar.make(search_root, it?.message ?: "Unable to process", Snackbar.LENGTH_SHORT)
                    .show();
                return@observe
            }

            ResultsActivity.startIntent(this, it.photos, edit_query)
        }

        viewModel.searchQueries.observeNotNulls {
            past_search_recycler_view.adapter = SearchAdapter(it) {
                edit_query.text = it.searchTerm
                fetchPhotos(it.searchTerm)
            }
        }


    }

    fun fetchPhotos(searchString: String) = viewModel.fetchPhotos(searchString)
}


class SearchAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val text = itemView.findViewById<TextView>(R.id.past_search_text)

    fun bind(searchQuery: SearchQuery) {

        text.text = searchQuery.searchTerm
    }

}

class SearchAdapter(val searchQueries: List<SearchQuery>, val searchClicked: (SearchQuery) -> Unit) :
    RecyclerView.Adapter<SearchAdapterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.past_search_view, parent, false)
        return SearchAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapterHolder, position: Int) {
        val query = searchQueries[position]
        holder.bind(query)
        holder.itemView.setOnClickListener {
            searchClicked(query)
        }

    }

    override fun getItemCount() = searchQueries.size

}

class SearchViewModel : BaseViewModel(){

    val photosResultsLiveData = MutableLiveData<PhotoSearchResults>()

    val searchQueries = MutableLiveData<List<SearchQuery>>()


    fun init() {
        scope.launch {
            searchQueries.postValue(flickerDB.searchQueryDao().getAll())
        }
    }

    fun fetchPhotos(searchString : String){
        scope.launch {
            val photoResults = PhotoRepository.getPhotos(searchString)
            photosResultsLiveData.postValue(photoResults)
        }
    }

}
