package com.yichiuan.moedict.ui.main

import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.yichiuan.moedict.R
import com.yichiuan.moedict.data.MoeRepository
import com.yichiuan.moedict.ui.RepositoryViewModelFactory
import com.yichiuan.moedict.ui.search.SearchActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import moe.Word
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var moeRepository: MoeRepository

    private lateinit var model: MoeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model = ViewModelProviders.of(this, RepositoryViewModelFactory(moeRepository))
                .get(MoeViewModel::class.java)

        setSupportActionBar(toolbar)

        recyclerview_heteronym.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false)
        recyclerview_heteronym.setHasFixedSize(true)

        val query = intent.getStringExtra(SearchManager.QUERY)
        if (query != null) {
            showWord(query)
        } else {
            showWord("萌")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_search) {
            SearchActivity.startSearch(this)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showWord(query: String) {

        var word: Word? = null
        try {
            word = model.getMoeWord(query)
        } catch (e: IOException) {
            Timber.e(e)
        }

        if (word == null) return

        val adapter = recyclerview_heteronym.adapter as HeteronymAdapter?

        if (adapter == null) {
            recyclerview_heteronym.adapter = HeteronymAdapter(this, word, lifecycle)
        } else {
            adapter.setWord(word)
        }

        recyclerview_heteronym.post { recyclerview_heteronym.scrollToPosition(0) }
    }
}
