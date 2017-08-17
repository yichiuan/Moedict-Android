package com.yichiuan.moedict.ui.search;

import android.arch.lifecycle.ViewModel;

import com.yichiuan.moedict.data.MoeRepository;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;

public class SearchViewModel extends ViewModel {

    private MoeRepository moeRepository;

    private final CompositeDisposable disposable = new CompositeDisposable();

    public SearchViewModel(MoeRepository moeRepository) {
        this.moeRepository = moeRepository;
    }

    public Completable loadIndexData() {
        return moeRepository.loadIndexData();
    }

    public ArrayList<Integer> search(String query) {
        return moeRepository.search(query);
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
