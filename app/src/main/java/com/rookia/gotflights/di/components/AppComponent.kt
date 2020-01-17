package com.rookia.gotflights.di.components

import com.rookia.gotflights.GoTFlightsApplication
import com.rookia.gotflights.di.modules.*
import com.rookia.gotflights.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [(AndroidSupportInjectionModule::class), (ActivityBuilder::class), (BindsModule::class),
        (ProvidesModule::class), (FragmentsProvider::class), (FragmentsProvider::class), (ViewModelModule::class)]
)
interface AppComponent : AndroidInjector<GoTFlightsApplication> {

    override fun inject(goTFlightsApp: GoTFlightsApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: GoTFlightsApplication): Builder

        fun build(): AppComponent
    }

}