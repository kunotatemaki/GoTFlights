package com.rookia.gotflights.di.modules

import android.content.Context
import com.rookia.gotflights.GoTFlightsApplication
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.domain.model.FlightsCache
import com.rookia.gotflights.framework.network.NetworkServiceFactory
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import com.rookia.gotflights.ui.main.FlightsViewModel
import com.rookia.gotflights.usecases.GetFlightsUseCase
import com.rookia.gotflights.usecases.FilterUseCase
import com.rookia.gotflights.utils.RateLimiter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class ProvidesModule {

    @Singleton
    @Provides
    fun providesRateLimiter(): RateLimiter = RateLimiter()

    @Provides
    fun providesContext(application: GoTFlightsApplication): Context =
        application.applicationContext

    @Singleton
    @Provides
    fun provideDb(
        context: Context
    ): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    fun provideMainViewModel(
        getFlightsUseCase: GetFlightsUseCase,
        filterUseCase: FilterUseCase
    ): FlightsViewModel = FlightsViewModel(
        getFlightsUseCase,
        filterUseCase
    )

    @Provides
    @Singleton
    fun providesGetFlightsUseCase(repository: Repository): GetFlightsUseCase =
        GetFlightsUseCase(repository)

    @Provides
    @Singleton
    fun providesFiltersUseCase(): FilterUseCase = FilterUseCase()

    @Provides
    @Singleton
    fun providesFlightsCache() = FlightsCache()

    @Singleton
    @Provides
    fun provideNetworkServiceFactory(): NetworkServiceFactory = NetworkServiceFactory()

}