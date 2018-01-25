package com.yichiuan.moedict.data

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataModule {
    @Provides
    @Singleton
    fun provideMoeRepository(context: Context): MoeRepository {
        return MoeRepository(context)
    }
}
