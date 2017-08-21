package com.yichiuan.moedict.ui.search;

import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.text.TextUtils;

import com.jakewharton.rx.ReplayingShare;
import com.yichiuan.moedict.data.MoeRepository;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class SearchViewModel extends ViewModel {

    private MoeRepository moeRepository;

    private final CompositeDisposable disposable = new CompositeDisposable();

    private String currentQuery = "";
    private Observable<Pair<String, ArrayList<Integer>>> result;

    public SearchViewModel(MoeRepository moeRepository) {
        this.moeRepository = moeRepository;
    }

    public Completable loadIndexData() {
        return moeRepository.loadIndexData();
    }

    public Observable<Pair<String, ArrayList<Integer>>> search(@NonNull String query) {
        if (!TextUtils.isEmpty(query) && query.equals(currentQuery) && result != null) {
            return result;
        }

        currentQuery = query;

        result = Observable.just(Pair.create(query, moeRepository.search(query)))
                .compose(ReplayingShare.instance())
                .take(1); // when re-subscribing, ReplayingShare emits onNext() twice

        return result;
    }

    public String getWord(int position) {
        return moeRepository.getWord(position);
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
