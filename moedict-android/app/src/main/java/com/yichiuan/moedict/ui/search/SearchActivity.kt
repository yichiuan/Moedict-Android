package com.yichiuan.moedict.ui.search

import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.MainThread
import android.support.v4.app.NavUtils
import android.support.v4.util.Pair
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import com.yichiuan.moedict.R
import com.yichiuan.moedict.data.MoeRepository
import com.yichiuan.moedict.ui.RepositoryViewModelFactory
import com.yichiuan.moedict.ui.main.MainActivity
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    companion object {

        private const val QUERY_STRING_KEY = "query"
        private const val LIST_STATE_KEY = "recycler_list_state"

        fun startSearch(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    internal lateinit var moeRepository: MoeRepository

    internal lateinit var model: SearchViewModel

    private lateinit var resultAdapter: ResultAdapter

    private var queryForResult: String? = ""

    private var storedQuery: String? = null
    private var storedListState: Parcelable? = null

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        model = ViewModelProviders.of(this, RepositoryViewModelFactory(moeRepository))
                .get(SearchViewModel::class.java)

        setResultView()

        imagebutton_search_back.setOnClickListener { NavUtils.navigateUpFromSameTask(this) }

        savedInstanceState?.let {
            storedQuery = it.getString(QUERY_STRING_KEY)
            storedListState = it.getParcelable(LIST_STATE_KEY)
        }

        disposable.add(model.loadIndexData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { this.setupSearchView() })
    }

    private fun setResultView() {
        resultAdapter = ResultAdapter(this, model)

        val resultRecyclerView = recyclerview_search_result

        resultRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)
        resultRecyclerView.addItemDecoration(
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        resultRecyclerView.setHasFixedSize(true)
        resultRecyclerView.adapter = resultAdapter

        if (storedListState != null) {
            resultRecyclerView.layoutManager.onRestoreInstanceState(storedListState)
        }

        resultRecyclerView.visibility = View.GONE
    }

    private fun setupSearchView() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search_view.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        disposable.add(RxSearchView.queryTextChanges(search_view)
                .doOnNext { query ->
                    if (query.isEmpty()) {
                        hideResult()
                    }
                }
                .filter { query -> query.isNotEmpty() }
                .observeOn(Schedulers.computation())
                .flatMap { query -> model.search(query.toString()) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { this.handleResult(it) })
    }

    private fun handleSearchIntent(intent: Intent) {
        val results = resultAdapter.results
        if (results == null || results.isEmpty()) {
            return
        }

        val query = intent.getStringExtra(SearchManager.QUERY)
        val show = Intent(this, MainActivity::class.java)
        show.putExtra(SearchManager.QUERY, query)
        startActivity(show)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (Intent.ACTION_SEARCH == intent.action) {
            handleSearchIntent(intent)
        }
    }

    private fun handleResult(result: Pair<String, ArrayList<Int>>) {
        queryForResult = result.first

        if (result.second!!.isEmpty()) {
            showNoResult()
        } else {
            showResult(result.second)
        }

        storedListState = null
        storedQuery = null
    }

    private fun showNoResult() {
        recyclerview_search_result.visibility = View.GONE
        textview_search_no_result.visibility = View.VISIBLE
    }

    private fun showResult(results: ArrayList<Int>?) {
        resultAdapter.results = results

        val resultRecyclerView = recyclerview_search_result
        resultRecyclerView.visibility = View.VISIBLE

        if (storedListState != null && storedQuery == queryForResult) {
            resultRecyclerView.layoutManager.onRestoreInstanceState(storedListState)
        } else {
            resultRecyclerView.scrollToPosition(0)
        }

        textview_search_no_result.visibility = View.GONE
    }

    @MainThread
    private fun hideResult() {
        recyclerview_search_result.visibility = View.GONE
        recyclerview_search_result.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        // Save list state
        outState?.let {
            it.putString(QUERY_STRING_KEY, queryForResult)
            it.putParcelable(LIST_STATE_KEY,
                    recyclerview_search_result.layoutManager.onSaveInstanceState())
        }
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }
}
