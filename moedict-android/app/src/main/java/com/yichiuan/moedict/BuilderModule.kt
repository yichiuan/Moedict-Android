package com.yichiuan.moedict

import com.yichiuan.moedict.ui.main.MainActivity
import com.yichiuan.moedict.ui.search.SearchActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class BuilderModule {
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindSearchActivity(): SearchActivity
}
