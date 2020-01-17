package com.rookia.gotflights.di.components

import com.rookia.gotflights.GoTFlightsApplication


object ComponentFactory {

    fun component(context: GoTFlightsApplication): AppComponent {
        return DaggerAppComponent.builder()
                .application(context)
                .build()
    }

}