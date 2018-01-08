package com.yichiuan.moedict.ui.main;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.yichiuan.moedict.R;
import com.yichiuan.moedict.data.MoeRepository;
import com.yichiuan.moedict.ui.RepositoryViewModelFactory;
import com.yichiuan.moedict.ui.search.SearchActivity;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import moe.Word;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Inject
    MoeRepository moeRepository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerview_heteronym)
    RecyclerView heteronymRecyclerView;

    MoeViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        model = ViewModelProviders.of(this, new RepositoryViewModelFactory(moeRepository))
                .get(MoeViewModel.class);

        setSupportActionBar(toolbar);

        heteronymRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        heteronymRecyclerView.setHasFixedSize(true);

        Intent intent = getIntent();
        String query = intent.getStringExtra(SearchManager.QUERY);
        if (query != null) {
            showWord(query);
        } else {
            showWord("萌");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            SearchActivity.startSearch(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showWord(String query) {

        Word word = null;
        try {
            word = model.getMoeWord(query);
        } catch (IOException e) {
            Timber.e(e);
        }

        if (word == null) return;

        HeteronymAdapter adapter = (HeteronymAdapter) heteronymRecyclerView.getAdapter();

        if (adapter == null) {
            heteronymRecyclerView.setAdapter(new HeteronymAdapter(this, word, getLifecycle()));
        } else {
            adapter.setWord(word);
        }

        heteronymRecyclerView.post(()-> heteronymRecyclerView.scrollToPosition(0));
    }
}
