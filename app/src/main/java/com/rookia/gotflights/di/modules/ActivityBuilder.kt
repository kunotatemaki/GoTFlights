package com.rookia.gotflights.di.modules

import com.rookia.gotflights.di.interfaces.CustomScopes
import com.rookia.gotflights.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @CustomScopes.ActivityScope
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity


}