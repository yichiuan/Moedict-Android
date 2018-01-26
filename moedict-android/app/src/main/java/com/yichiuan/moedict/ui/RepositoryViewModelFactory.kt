package com.yichiuan.moedict.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.yichiuan.moedict.data.MoeRepository
import com.yichiuan.moedict.ui.main.MoeViewModel
import com.yichiuan.moedict.ui.search.SearchViewModel

class RepositoryViewModelFactory(private val moeRepository: MoeRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(moeRepository) as T
        } else if (modelClass.isAssignableFrom(MoeViewModel::class.java)) {
            return MoeViewModel(moeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
