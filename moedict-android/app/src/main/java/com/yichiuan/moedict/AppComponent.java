package com.yichiuan.moedict;

import com.yichiuan.moedict.data.DataModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        DataModule.class,
        BuilderModule.class})
public interface AppComponent {
    void inject(MoeApplication application);
}
