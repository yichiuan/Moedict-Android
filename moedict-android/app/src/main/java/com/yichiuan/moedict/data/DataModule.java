package com.yichiuan.moedict.data;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class DataModule {
    @Provides
    @Singleton
    MoeRepository provideMoeRepository(Context context) {
        return new MoeRepository(context);
    }
}
