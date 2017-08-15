package com.yichiuan.moedict;

import com.yichiuan.moedict.ui.main.MainActivity;
import com.yichiuan.moedict.ui.search.SearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class BuilderModule {
    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract SearchActivity bindSearchActivity();
}
