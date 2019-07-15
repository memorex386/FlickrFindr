package com.example.flickerexample.ui.search

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.example.flickerexample.R
import com.example.flickerexample.core.base.BaseViewModel
import com.example.flickerexample.core.base.BaseViewModelActivity
import com.example.flickerexample.core.extensions.textString
import com.example.flickerexample.models.PhotoSearchResults
import com.example.flickerexample.models.isSuccessful
import com.example.flickerexample.network.PhotoRepository
import com.example.flickerexample.ui.results.ResultsActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.launch


class SearchActivity : BaseViewModelActivity<SearchViewModel>(SearchViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        search_button.setOnClickListener {
            var searchString = edit_query.textString
            if (edit_query.textString.isNullOrBlank()){
                return@setOnClickListener
            }

            viewModel.fetchPhotos(searchString)

        }

        viewModel.photosResultsLiveData.observe {
            if (it?.isSuccessful != true || it?.photos == null) {
                Snackbar.make(search_root, it?.message ?: "Unable to process", Snackbar.LENGTH_SHORT)
                    .show();
                return@observe
            }

            startActivity(ResultsActivity.getIntent(this, it.photos))
        }
    }
}

class SearchViewModel : BaseViewModel(){

    val photosResultsLiveData = MutableLiveData<PhotoSearchResults>()

    fun fetchPhotos(searchString : String){
        scope.launch {
            val photoResults = PhotoRepository.getPhotos(searchString)
            photosResultsLiveData.postValue(photoResults)
        }
    }

}
