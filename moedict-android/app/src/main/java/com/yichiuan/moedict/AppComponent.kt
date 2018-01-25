package com.yichiuan.moedict

import com.yichiuan.moedict.data.DataModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AndroidSupportInjectionModule::class, AppModule::class,
        DataModule::class, BuilderModule::class))
interface AppComponent {
    fun inject(application: MoeApplication)
}
