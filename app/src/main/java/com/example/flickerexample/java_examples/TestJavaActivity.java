package com.example.flickerexample.java_examples;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flickerexample.R;
import com.example.flickerexample.core.extensions.LifeCycleExtensionsKt;
import com.example.flickerexample.models.photos.PhotoSearchResults;
import com.example.flickerexample.models.photos.PhotoSearchResultsKt;
import com.example.flickerexample.models.photos.SearchQuery;
import com.example.flickerexample.network.PhotoRepository;
import com.example.flickerexample.network.Result;
import com.example.flickerexample.ui.bookmarks.BookmarkedActivity;
import com.example.flickerexample.ui.results.ResultsActivity;
import com.example.flickerexample.ui.search.BaseSearchActivity;
import com.example.flickerexample.ui.search.SearchAdapter;
import com.example.flickerexample.ui.views.CustomSearchView;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestJavaActivity extends BaseSearchActivity<TestJavaViewModel> {

    private CustomSearchView customSearchView;
    private Button searchButton;
    private Button bookmarkButton;
    private RecyclerView recyclerView;

    public TestJavaActivity() {
        super(TestJavaViewModel.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        customSearchView = findViewById(R.id.edit_query);
        searchButton = findViewById(R.id.search_button_now);
        bookmarkButton = findViewById(R.id.bookmark_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySearch(customSearchView.getText());
            }
        });

        customSearchView.onActionSearch(new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {
                trySearch(s);
                return null;
            }
        });

        getViewModel().getPhotosFetchedAction().observe(this, new Function1<Result<PhotoSearchResults>, Unit>() {
            @Override
            public Unit invoke(Result<PhotoSearchResults> photoSearchResultsResult) {

                showLoading(false);

                if (photoSearchResultsResult instanceof Result.Error) {
                    snackBar(((Result.Error) photoSearchResultsResult).getException().getLocalizedMessage());
                } else if (photoSearchResultsResult instanceof Result.Success) {
                    PhotoSearchResults result = PhotoRepository.INSTANCE.getLastResult().getValue();
                    if (result == null || result.getPhotos() == null || !PhotoSearchResultsKt.isSuccessful(result)) {
                        snackBar(result.getMessage() != null ? result.getMessage() : "Unable to process");
                        return null;
                    }

                    ResultsActivity.Companion.startIntent(TestJavaActivity.this, result, customSearchView);
                }

                return null;
            }
        });


        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BookmarkedActivity.Companion.getIntent(TestJavaActivity.this));
            }
        });

        LifeCycleExtensionsKt.addStart(getLifecycle(), new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getViewModel().fetchSearchQueries();

                singleObserveNotNull(getViewModel().getSearchQueries(), new Function1<List<SearchQuery>, Unit>() {
                    @Override
                    public Unit invoke(List<SearchQuery> searchQueries) {
                        return null;
                    }
                });

                PhotoSearchResults value = PhotoRepository.INSTANCE.getLastResult().getValue();

                customSearchView.setText(value == null || value.getSearchTerm() == null ? "" : value.getSearchTerm());

                return null;
            }
        });


    }

    /**
     * pass search query list
     *
     * @param list search query list
     */
    private void searchQueriesUpdated(List<SearchQuery> list) {
        if (list == null || list.size() == 0) return;

        recyclerView.setAdapter(new SearchAdapter(list, new Function1<SearchQuery, Unit>() {
            @Override
            public Unit invoke(SearchQuery searchQuery) {
                customSearchView.setText(searchQuery.getSearchTerm());
                trySearch(searchQuery.getSearchTerm());
                return null;
            }
        }));
    }


    @NotNull
    @Override
    public View rootView() {
        return findViewById(R.id.search_root);
    }

    @Nullable
    @Override
    public View loading() {
        return findViewById(R.id.loading);
    }

    @NotNull
    @Override
    public CustomSearchView searchBox() {
        return findViewById(R.id.edit_query);
    }
}
