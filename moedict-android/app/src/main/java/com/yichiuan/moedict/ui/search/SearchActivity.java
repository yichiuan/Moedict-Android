package com.yichiuan.moedict.ui.search;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.MainThread;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.yichiuan.moedict.R;
import com.yichiuan.moedict.data.MoeRepository;
import com.yichiuan.moedict.ui.main.MainActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    public final static String QUERY_STRING_KEY = "query";
    public final static String LIST_STATE_KEY = "recycler_list_state";

    @Inject
    MoeRepository moeRepository;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.recyclerview_search_result)
    RecyclerView resultRecyclerView;

    @BindView(R.id.textview_search_no_result)
    TextView noResultTextView;

    @BindView(R.id.imagebutton_search_back)
    ImageButton backButton;

    SearchViewModel model;

    ResultAdapter resultAdapter;

    String queryForResult = "";

    String storedQuery;
    Parcelable storedListState;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public static void startSearch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        model = ViewModelProviders.of(this, new SearchViewModelFactory(moeRepository))
                .get(SearchViewModel.class);

        setResultView();

        backButton.setOnClickListener(v -> {
            finish();
        });

        if (savedInstanceState != null) {
            storedQuery = savedInstanceState.getString(QUERY_STRING_KEY);
            storedListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }

        disposable.add(model.loadIndexData()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(() -> {
                   setupSearchView();
               }));
    }

    private void setResultView() {
        resultAdapter = new ResultAdapter(this, model);

        resultRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        resultRecyclerView.setHasFixedSize(true);
        resultRecyclerView.setAdapter(resultAdapter);

        if (storedListState != null) {
            resultRecyclerView.getLayoutManager().onRestoreInstanceState(storedListState);
        }

        resultRecyclerView.setVisibility(View.GONE);
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        disposable.add(RxSearchView.queryTextChanges(searchView)
                .doOnNext(query -> {
                    if (query.length() == 0) {
                        hideResult();
                    }
                })
                .filter(query -> query.length() > 0)
                .observeOn(Schedulers.computation())
                .flatMap(query -> model.search(query.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    handleResult(result);
                }));
    }

    private void handleSearchIntent(Intent intent) {
        if (resultAdapter.getResults() == null || resultAdapter.getResults().isEmpty()) {
            return;
        }

        String query = intent.getStringExtra(SearchManager.QUERY);
        Intent show = new Intent(this, MainActivity.class);
        show.putExtra(SearchManager.QUERY, query);
        startActivity(show);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            handleSearchIntent(intent);
        }
    }

    private void handleResult(Pair<String, ArrayList<Integer>> result) {
        queryForResult = result.first;

        if (result.second.isEmpty()) {
            showNoResult();
        } else {
            showResult(result.second);
        }

        storedListState = null;
        storedQuery = null;
    }

    private void showNoResult() {
        resultRecyclerView.setVisibility(View.GONE);
        noResultTextView.setVisibility(View.VISIBLE);
    }

    private void showResult(ArrayList<Integer> results) {
        resultAdapter.setResults(results);
        resultRecyclerView.setVisibility(View.VISIBLE);

        if (storedListState != null && storedQuery.equals(queryForResult)) {
            resultRecyclerView.getLayoutManager().onRestoreInstanceState(storedListState);
        } else {
            resultRecyclerView.scrollToPosition(0);
        }

        noResultTextView.setVisibility(View.GONE);
    }

    @MainThread
    private void hideResult() {
        resultRecyclerView.setVisibility(View.GONE);
        noResultTextView.setVisibility(View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save list state
        outState.putString(QUERY_STRING_KEY, queryForResult);
        Parcelable listState = resultRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
