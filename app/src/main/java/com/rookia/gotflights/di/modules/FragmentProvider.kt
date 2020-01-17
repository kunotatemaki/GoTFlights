package com.rookia.gotflights.di.modules

import com.rookia.gotflights.di.interfaces.CustomScopes
import com.rookia.gotflights.ui.checkout.CheckoutDialogFragment
import com.rookia.gotflights.ui.main.FlightsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentsProvider {

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun providesMainFragmentFactory(): FlightsFragment

    @CustomScopes.FragmentScope
    @ContributesAndroidInjector
    abstract fun providesCheckoutFragmentFactory(): CheckoutDialogFragment

}