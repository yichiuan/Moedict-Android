package com.yichiuan.moedict.ui.main;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.yichiuan.moedict.R;
import com.yichiuan.moedict.data.MoeRepository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import moe.Word;

public class MainActivity extends AppCompatActivity {

    @Inject
    MoeRepository moeRepository;

    @BindView(R.id.recyclerview_heteronym)
    RecyclerView heteronymRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        heteronymRecyclerview.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        heteronymRecyclerview.setHasFixedSize(true);

        heteronymRecyclerview.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Assumes current activity is the searchable activity
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Word word = moeRepository.getMoeWord(query);

            if (word == null) return;

            HeteronymAdapter adapter = (HeteronymAdapter) heteronymRecyclerview.getAdapter();

            if (adapter == null) {
                heteronymRecyclerview.setAdapter(new HeteronymAdapter(this, word));
            } else {
                adapter.setWord(word);
            }

            heteronymRecyclerview.scrollToPosition(0);
            heteronymRecyclerview.setVisibility(View.VISIBLE);
        }
    }
}
