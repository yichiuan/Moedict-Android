package com.yichiuan.moedict.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;

import com.yichiuan.moedict.R;
import com.yichiuan.moedict.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.search_view)
    SearchView searchView;

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

        setupSearchView();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            handleSearchIntent(intent);
        }
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
    }

    private void handleSearchIntent(Intent intent) {
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
}
