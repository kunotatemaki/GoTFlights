package com.rookia.gotflights

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.whenever
import com.rookia.gotflights.domain.network.model.ApiResponse
import com.rookia.gotflights.framework.network.GotExchangeRatesApi
import com.rookia.gotflights.framework.network.GotFlightsApi
import com.rookia.gotflights.framework.network.NetworkServiceFactory
import com.rookia.gotflights.ui.MainActivity
import com.rookia.gotflights.ui.main.FlightsFragment
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import retrofit2.Response
import testclasses.getFlightFromNetwork


@RunWith(AndroidJUnit4::class)
class FiltersInstrumentedTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Rule
    @JvmField
    var rule = espressoDaggerMockRule()

    @Mock
    private lateinit var flightsApi: GotFlightsApi

    @Mock
    private lateinit var exchangeApi: GotExchangeRatesApi

    @Mock
    private lateinit var network: NetworkServiceFactory

    private val flight1 = getFlightFromNetwork(
        "Valladolid",
        "Madrid",
        12.toDouble()
    )
    private val flight2 = getFlightFromNetwork(
        "Zamora",
        "Sevilla",
        1.toDouble()
    )
    private val flight3 = getFlightFromNetwork(
        "Valladolid",
        "Madrid",
        11.toDouble()
    )
    private val flight4 = getFlightFromNetwork(
        "Zamora",
        "Sevilla",
        10.toDouble()
    )
    private val flight5 = getFlightFromNetwork(
        "Valladolid",
        "Madrid",
        10.toDouble()
    )

    @Before
    fun setUp() {
        whenever(network.getFlightsServiceInstance()).thenReturn(flightsApi)
        whenever(network.getExchangeRatesServiceInstance()).thenReturn(exchangeApi)
    }


    @Test
    fun showAndHideFilter() {
        runBlocking {
            whenever(flightsApi.getFlights()).thenReturn(
                Response.success(
                    ApiResponse(
                        listOf(
                            flight1,
                            flight2
                        )
                    )
                )
            )

            val activity = activityRule.launchActivity(null)
            val fragment = FlightsFragment()
            val transaction = activity.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment, "")
            transaction.commit()
            //mocks will be injected by dagger

            onView(withId(R.id.filters_container)).check(matches(CoreMatchers.not(isDisplayed())))
            onView(withId(R.id.action_filter)).perform(click())
            onView(withId(R.id.filters_container)).check(matches(isDisplayed()))
            onView(withId(R.id.filters_apply)).perform(click())
            onView(withId(R.id.filters_container)).check(matches(CoreMatchers.not(isDisplayed())))
        }
    }

}
