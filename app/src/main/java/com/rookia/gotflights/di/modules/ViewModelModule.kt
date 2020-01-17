package com.rookia.gotflights.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rookia.gotflights.di.interfaces.ViewModelKey
import com.rookia.gotflights.ui.checkout.CheckoutViewModel
import com.rookia.gotflights.ui.common.ViewModelFactory
import com.rookia.gotflights.ui.main.FlightsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FlightsViewModel::class)
    internal abstract fun bindMainViewModel(flightsViewModel: FlightsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckoutViewModel::class)
    internal abstract fun bindCheckoutViewModel(checkoutViewModel: CheckoutViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}