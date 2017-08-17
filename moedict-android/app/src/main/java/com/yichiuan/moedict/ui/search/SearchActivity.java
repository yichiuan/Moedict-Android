package com.yichiuan.moedict.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
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
import moe.Index;
import timber.log.Timber;

public class SearchActivity extends AppCompatActivity {

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

    private Index index;

    ResultAdapter resultAdapter;

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

        index = moeRepository.getIndex();

        setupSearchView();
        setResultView();

        backButton.setOnClickListener(v -> {
            finish();
        });


        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            handleSearchIntent(intent);
        }
    }

    private void setResultView() {
        resultAdapter = new ResultAdapter(this, index);

        resultRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        resultRecyclerView.setHasFixedSize(true);
        resultRecyclerView.setAdapter(resultAdapter);
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
                .map(query -> index.search(query.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(results -> {
                    Timber.d("result count = " + results.size());

                    if (results.isEmpty()) {
                        showNoResult();
                    } else {
                        showResults(results);
                    }
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

    private void showNoResult() {
        resultRecyclerView.setVisibility(View.GONE);
        noResultTextView.setVisibility(View.VISIBLE);
    }

    private void showResults(ArrayList<Integer> results) {
        resultAdapter.setResults(results);
        resultRecyclerView.scrollToPosition(0);
        resultRecyclerView.setVisibility(View.VISIBLE);

        noResultTextView.setVisibility(View.GONE);
    }

    @MainThread
    private void hideResult() {
        resultRecyclerView.setVisibility(View.GONE);
        noResultTextView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        disposable.clear();
        super.onDestroy();
    }
}
