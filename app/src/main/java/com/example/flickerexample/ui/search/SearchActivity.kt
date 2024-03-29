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
import com.example.flickerexample.core.base.ResultLiveDataAction
import com.example.flickerexample.core.extensions.addStart
import com.example.flickerexample.core.extensions.animateFade
import com.example.flickerexample.core.extensions.post
import com.example.flickerexample.models.photos.PhotoSearchResults
import com.example.flickerexample.models.photos.SearchQuery
import com.example.flickerexample.models.photos.isSuccessful
import com.example.flickerexample.network.PhotoRepository
import com.example.flickerexample.network.Result
import com.example.flickerexample.room.flickerDB
import com.example.flickerexample.ui.bookmarks.BookmarkedActivity
import com.example.flickerexample.ui.results.ResultsActivity
import com.example.flickerexample.ui.views.CustomSearchView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.coroutines.launch


class SearchActivity : BaseSearchActivity<SearchViewModel>(SearchViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        search_button_now.setOnClickListener {
            trySearch(edit_query.text)
        }

        edit_query.onActionSearch(::trySearch)

        viewModel.photosFetchedAction.observe {
            showLoading(false)

            it

                .ifError {
                    snackBar(it.localizedMessage)
                }

                .ifSuccess {
                    val result = PhotoRepository.lastResult.value
                    if (result?.isSuccessful != true || result.photos == null) {
                        snackBar(result?.message ?: "Unable to process")
                        return@observe
                    }

                    ResultsActivity.startIntent(this, result, edit_query)
                }


        }

        bookmark_button.setOnClickListener {
            startActivity(BookmarkedActivity.getIntent(this))
        }

        lifecycle.addStart {

            viewModel.fetchSearchQueries()

            viewModel.searchQueries.singleObserveNotNull(::searchQueriesUpdated)

            edit_query.text = PhotoRepository.lastResult.value?.searchTerm ?: ""
        }
    }

    override fun rootView() = search_root

    override fun loading() = loading

    override fun searchBox() = edit_query

    /**
     * pass search query list
     * @param list search query list
     */
    private fun searchQueriesUpdated(list: List<SearchQuery>) {
        if (list.isEmpty()) {
            return
        }
        past_search_recycler_view.adapter = SearchAdapter(list) {
            edit_query.text = it.searchTerm
            trySearch(it.searchTerm)
        }
    }


}

abstract class BaseSearchActivity<T : SearchViewModel>(_viewModelType: Class<T>) :
    BaseViewModelActivity<T>(_viewModelType) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addStart {
            searchBox().onSortChanged = {
                trySearch()
            }
        }
    }

    fun snackBar(err: String) = Snackbar.make(rootView(), err, Snackbar.LENGTH_SHORT)
        .show();

    fun trySearch(searchString: String = PhotoRepository.lastResult.value?.searchTerm ?: "") {
        if (searchString.isBlank()) {
            Snackbar.make(rootView(), getString(R.string.invalid_search), Snackbar.LENGTH_SHORT)
                .show();
            return
        }

        showLoading(true)


        viewModel.fetchPhotos(searchString)
    }

    fun showLoading(show: Boolean) = loading()?.animateFade(show)

    abstract fun rootView(): View
    abstract fun loading(): View?
    abstract fun searchBox(): CustomSearchView

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

open class SearchViewModel : BaseViewModel() {

    val searchQueries = MutableLiveData<List<SearchQuery>>()

    val photosFetchedAction = ResultLiveDataAction<Result<PhotoSearchResults>>()

    val isLoading = MutableLiveData<Boolean>()

    private suspend fun asyncSearchQueries() =
        flickerDB.searchQueryDao().getAll()

    /**
     * async fetch search Queries
     */
    fun fetchSearchQueries() {
        scope.launch {
            val queries = asyncSearchQueries()
            if (queries.isEmpty()) {

                //TODO : Added this to offer some recent history right off the bat
                val newList = arrayOf(
                    SearchQuery("cat"),
                    SearchQuery("dog"),
                    SearchQuery("man"),
                    SearchQuery("Aaron Rodgers"),
                    SearchQuery("catdog"),
                    SearchQuery("turkey"),
                    SearchQuery("zebra")
                )
                flickerDB.searchQueryDao().insert(*newList)
                searchQueries.post = newList.toList()

            } else
                searchQueries.post = (queries)
        }
    }

    fun fetchPhotos(searchString: String) {
        isLoading.value = true
        scope.launch {
            val result = PhotoRepository.getPhotos(searchString)
            asyncSearchQueries()
            photosFetchedAction.postTrigger(result)
            isLoading.post = false
        }
    }

}
