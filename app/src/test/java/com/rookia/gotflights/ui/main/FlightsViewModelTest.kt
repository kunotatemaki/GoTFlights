package com.rookia.gotflights.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.usecases.GetFlightsUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import testclasses.getFlight


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class FlightsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @MockK
    private lateinit var getFlightsUseCase: GetFlightsUseCase

    private lateinit var viewModel: FlightsViewModel

    private val flight1 = getFlight(
        "Valladolid",
        "Madrid",
        1.toBigDecimal()
    ).also { it.setExchangeRate(1.toBigDecimal()) }
    private val flight2 = getFlight(
        "Zamora",
        "Sevilla",
        2.toBigDecimal()
    ).also { it.setExchangeRate(1.toBigDecimal()) }
    private val flight3 = getFlight(
        "Valladolid",
        "Madrid",
        3.toBigDecimal()
    ).also { it.setExchangeRate(1.toBigDecimal()) }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(mainThreadSurrogate)
        viewModel = FlightsViewModel(
            getFlightsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun refresh() {
        runBlocking(Dispatchers.IO) {
            val triggerObserver = viewModel.fetchTrigger.test()
            viewModel.refresh()

            viewModel
            triggerObserver
                .assertHasValue()
                .assertHistorySize(2)

        }
    }

    @Test
    fun `store max and min prices from an ordered list of more than 2 items`(){
        val orderedList = listOf(flight1, flight2, flight3)
        viewModel.storeMaxAndMinPrices(orderedList)
        Assert.assertEquals(1.toBigDecimal(), viewModel.minPrice)
        Assert.assertEquals(3.toBigDecimal(), viewModel.maxPrice)
    }

    @Test
    fun `store max and min prices from an ordered list of 2 items`(){
        val orderedList = listOf(flight1, flight2)
        viewModel.storeMaxAndMinPrices(orderedList)
        Assert.assertEquals(1.toBigDecimal(), viewModel.minPrice)
        Assert.assertEquals(2.toBigDecimal(), viewModel.maxPrice)
    }

    @Test
    fun `store max and min prices from an ordered list of 1 item`(){
        val orderedList = listOf(flight1)
        viewModel.storeMaxAndMinPrices(orderedList)
        Assert.assertEquals(1.toBigDecimal(), viewModel.minPrice)
        Assert.assertEquals(1.toBigDecimal(), viewModel.maxPrice)
    }

    @Test
    fun `store max and min prices from an ordered empty list`(){
        val orderedList = listOf<Flight>()
        viewModel.storeMaxAndMinPrices(orderedList)
        Assert.assertNull(viewModel.minPrice)
        Assert.assertNull(viewModel.maxPrice)
    }

}