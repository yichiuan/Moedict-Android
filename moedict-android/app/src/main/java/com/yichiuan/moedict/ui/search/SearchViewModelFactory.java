package com.yichiuan.moedict.ui.search;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.yichiuan.moedict.data.MoeRepository;

public class SearchViewModelFactory implements ViewModelProvider.Factory {

    private MoeRepository moeRepository;

    public SearchViewModelFactory(MoeRepository moeRepository) {
        this.moeRepository = moeRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(moeRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
