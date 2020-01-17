package com.rookia.gotflights.di.modules

import android.content.Context
import com.rookia.gotflights.GoTFlightsApplication
import com.rookia.gotflights.usecases.AddProductToBasketUseCase
import com.rookia.gotflights.usecases.ClearBasketUseCase
import com.rookia.gotflights.usecases.GetFlightsUseCase
import com.rookia.gotflights.usecases.RemoveProductFromBasketUseCase
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import com.rookia.gotflights.data.repository.Repository
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.domain.model.FlightsCache
import com.rookia.gotflights.ui.checkout.CheckoutViewModel
import com.rookia.gotflights.ui.main.FlightsViewModel
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
    fun providesContext(application: GoTFlightsApplication): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideDb(
        context: Context
    ): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    fun provideMainViewModel(
        getFlightsUseCase: GetFlightsUseCase,
        addProductToBasketUseCase: AddProductToBasketUseCase,
        removeProductFromBasketUseCase: RemoveProductFromBasketUseCase,
        clearBasketUseCase: ClearBasketUseCase
    ): FlightsViewModel = FlightsViewModel(
        getFlightsUseCase,
        addProductToBasketUseCase,
        removeProductFromBasketUseCase,
        clearBasketUseCase
    )

    @Provides
    fun provideCheckoutViewModel(
        getFlightsUseCase: GetFlightsUseCase,
        clearBasketUseCase: ClearBasketUseCase,
        resourcesManager: ResourcesManager
    ): CheckoutViewModel = CheckoutViewModel(getFlightsUseCase, clearBasketUseCase, resourcesManager)

    @Provides
    @Singleton
    fun providesGetProductsUseCase(repository: Repository): GetFlightsUseCase = GetFlightsUseCase(repository)

    @Provides
    @Singleton
    fun providesAddProductsToBasketUseCase(repository: Repository): AddProductToBasketUseCase =
        AddProductToBasketUseCase(repository)

    @Provides
    @Singleton
    fun providesRemoveProductsFromBasketUseCase(repository: Repository): RemoveProductFromBasketUseCase =
        RemoveProductFromBasketUseCase(repository)

    @Provides
    @Singleton
    fun providesClearBasketUseCase(repository: Repository): ClearBasketUseCase = ClearBasketUseCase(repository)

    @Provides
    @Singleton
    fun providesFlightsCache() = FlightsCache()

}