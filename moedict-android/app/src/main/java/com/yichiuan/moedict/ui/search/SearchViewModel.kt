package com.yichiuan.moedict.ui.search

import android.arch.lifecycle.ViewModel
import android.support.v4.util.Pair
import com.jakewharton.rx.ReplayingShare
import com.yichiuan.moedict.data.MoeRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class SearchViewModel(private val moeRepository: MoeRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    private var currentQuery = ""
    private var result: Observable<Pair<String, ArrayList<Int>>>? = null

    fun loadIndexData(): Completable {
        return moeRepository.loadIndexData()
    }

    fun search(query: String): Observable<Pair<String, ArrayList<Int>>>? {
        if (query.isNotEmpty() && query == currentQuery && result != null) {
            return result
        }

        currentQuery = query

        result = Observable.just(Pair.create(query, moeRepository.search(query)))
                .compose(ReplayingShare.instance())
                .take(1) // when re-subscribing, ReplayingShare emits onNext() twice

        return result
    }

    fun getWord(position: Int): String? {
        return moeRepository.getWord(position)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}
