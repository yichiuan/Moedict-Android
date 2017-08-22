package com.yichiuan.moedict.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.yichiuan.moedict.data.MoeRepository;
import com.yichiuan.moedict.ui.main.MoeViewModel;
import com.yichiuan.moedict.ui.search.SearchViewModel;

public class RepositoryViewModelFactory implements ViewModelProvider.Factory {

    private MoeRepository moeRepository;

    public RepositoryViewModelFactory(MoeRepository moeRepository) {
        this.moeRepository = moeRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(moeRepository);
        } else if (modelClass.isAssignableFrom(MoeViewModel.class)) {
            return (T) new MoeViewModel(moeRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
