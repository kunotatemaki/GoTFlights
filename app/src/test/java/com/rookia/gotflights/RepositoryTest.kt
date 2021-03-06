package com.rookia.gotflights

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jraska.livedata.test
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.model.FlightsCache
import com.rookia.gotflights.domain.network.model.ApiResponse
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.framework.network.GotFlightsApi
import com.rookia.gotflights.framework.network.NetworkServiceFactory
import com.rookia.gotflights.framework.repository.RepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import testclasses.getFlight
import testclasses.getFlightFromNetwork
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@ObsoleteCoroutinesApi
@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
class RepositoryTest {
    private val targetCurrencyName = "EUR"

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var networkServiceFactory: NetworkServiceFactory

    @MockK(relaxed = true)
    lateinit var flightsCache: FlightsCache

    @MockK(relaxed = true)
    lateinit var persistenceManager: PersistenceManager

    @MockK
    lateinit var api: GotFlightsApi

    @InjectMockKs
    private lateinit var repository: RepositoryImpl

    private lateinit var cacheResponse: List<Flight>
    private lateinit var networkResponse: ApiResponse
    private val flight1 = getFlight("Valladolid", "Zamora", 1.0.toDouble())
    private val flight2 = getFlight("Burgos", "León", 2.0.toDouble())
    private val flight3 = getFlight("Oviedo", "Salamanca", 3.0.toDouble())
    private val nFlight1 = getFlightFromNetwork("Valladolid", "Zamora", 1.0.toDouble())
    private val nFlight2 = getFlightFromNetwork("Burgos", "León", 2.0.toDouble())
    private val nFlight3 = getFlightFromNetwork("Oviedo", "Salamanca", 3.0.toDouble())


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        cacheResponse = listOf(flight1, flight2, flight3)
        networkResponse = ApiResponse(listOf(nFlight1, nFlight2, nFlight3))
        Dispatchers.setMain(mainThreadSurrogate)
        every { networkServiceFactory.getFlightsServiceInstance() } returns api
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `load data from cache and make a network call`() {
        runBlocking(Dispatchers.IO) {
            every { flightsCache.getListOfFlightsInCache() } returns
                    MutableLiveData<List<Flight>>().also {
                        it.value = cacheResponse
                    }

            coEvery { api.getFlights() } returns Response.success(200, networkResponse)

            val response = repository.getFlights(targetCurrencyName)
            val responseObserver = response.test()
            val latch = CountDownLatch(2)
            val observer = Observer<Result<List<Flight>>> {
                latch.countDown()
            }
            response.observeForever(observer)
            latch.await(10, TimeUnit.SECONDS)
            responseObserver
                .assertHasValue()
                .assertHistorySize(2)
                .assertValue(Result.success(cacheResponse))
                .assertValueHistory(Result.loading(cacheResponse), Result.success(cacheResponse))
        }
    }

    @Test
    fun `no data from db and network call`() {
        runBlocking(Dispatchers.IO) {
            every { flightsCache.getListOfFlightsInCache() } returns MutableLiveData<List<Flight>>().also {
                it.value = listOf()
            }

            coEvery { api.getFlights() } returns Response.success(200, networkResponse)

            val response = repository.getFlights(targetCurrencyName)
            val responseObserver = response.test()
            val latch = CountDownLatch(2)
            val observer = Observer<Result<List<Flight>>> {
                latch.countDown()
            }
            response.observeForever(observer)
            latch.await(10, TimeUnit.SECONDS)
            responseObserver
                .assertHasValue()
                .assertHistorySize(2)
                .assertValue(Result.success(listOf()))
                .assertValueHistory(
                    Result.loading(listOf()),
                    Result.success(listOf())
                )
        }
    }


    @Test
    fun `load data from db and error in the network call`() {
        runBlocking(Dispatchers.IO) {
            every { flightsCache.getListOfFlightsInCache() } returns
                    MutableLiveData<List<Flight>>().also {
                        it.value = cacheResponse
                    }

            coEvery { api.getFlights() } returns Response.error(
                500,
                "{\"message\":\"some_value\"}".toResponseBody(
                    "application/json".toMediaType()
                )
            )

            val response = repository.getFlights(targetCurrencyName)
            val responseObserver = response.test()
            val latch = CountDownLatch(2)
            val observer = Observer<Result<List<Flight>>> {
                latch.countDown()
            }
            response.observeForever(observer)
            latch.await(10, TimeUnit.SECONDS)
            responseObserver
                .assertHasValue()
                .assertHistorySize(2)
                .assertValue(Result.error("Response.error()", cacheResponse))
                .assertValueHistory(
                    Result.loading(cacheResponse),
                    Result.error("Response.error()", cacheResponse)
                )
        }
    }

    @Test
    fun `load data from db and exception in the network call`() {
        runBlocking(Dispatchers.IO) {
            every {
                flightsCache.getListOfFlightsInCache()
            } returns MutableLiveData<List<Flight>>().also {
                it.value = cacheResponse
            }

            coEvery { api.getFlights() } throws Exception()
        }

        val response = repository.getFlights(targetCurrencyName)
        val responseObserver = response.test()
        val latch = CountDownLatch(2)
        val observer =
            Observer<Result<List<Flight>>> {
                latch.countDown()
            }
        response.observeForever(observer)
        latch.await(10, TimeUnit.SECONDS)
        responseObserver
            .assertHasValue()
            .assertHistorySize(2)
            .assertValue(
                Result.error(
                    "error fetching from network",
                    cacheResponse
                )
            )
            .assertValueHistory(
                Result.loading(cacheResponse),
                Result.error(
                    "error fetching from network",
                    cacheResponse
                )
            )
    }

}
