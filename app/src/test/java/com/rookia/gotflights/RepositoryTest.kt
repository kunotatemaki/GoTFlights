package com.rookia.gotflights

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jraska.livedata.test
import com.rookia.gotflights.domain.network.AppApi
import com.rookia.gotflights.domain.network.AppApi.Products
import com.rookia.gotflights.data.network.NetworkServiceFactory
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.framework.persistence.entities.ProductEntity
import com.rookia.gotflights.data.preferences.PreferencesManager
import com.rookia.gotflights.framework.repository.RepositoryImpl
import com.rookia.gotflights.utils.RateLimiter
import com.rookia.gotflights.domain.vo.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@ObsoleteCoroutinesApi
@Suppress("BlockingMethodInNonBlockingContext")
@ExperimentalCoroutinesApi
class RepositoryTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var networkServiceFactory: NetworkServiceFactory

    @MockK(relaxed = true)
    lateinit var persistenceManager: PersistenceManager

    @MockK(relaxed = true)
    lateinit var preferencesManager: PreferencesManager

    @MockK
    lateinit var rateLimiter: RateLimiter

    @MockK
    lateinit var api: AppApi

    @InjectMockKs
    private lateinit var repository: RepositoryImpl

    private lateinit var dbResponse: List<ProductEntity>
    private lateinit var networkResponse: Products
    private val dbProduct1 = ProductEntity("code1", "name1", 1.0)
    private val dbProduct2 = ProductEntity("code2", "name2", 2.0)
    private val dbProduct3 = ProductEntity("code3", "name3", 3.0)

    private val networkProduct1 =
        AppApi.Item(code = dbProduct1.code, name = dbProduct1.name, price = dbProduct1.price)
    private val networkProduct2 =
        AppApi.Item(code = dbProduct2.code, name = dbProduct2.name, price = dbProduct2.price)
    private val networkProduct3 =
        AppApi.Item(code = dbProduct3.code, name = dbProduct3.name, price = dbProduct3.price)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dbResponse = mutableListOf(dbProduct1, dbProduct2, dbProduct3)
        networkResponse = Products(listOf(networkProduct1, networkProduct2, networkProduct3))
        Dispatchers.setMain(mainThreadSurrogate)
        every { networkServiceFactory.getServiceInstance() } returns api
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

//    @Test
//    fun `load data from db and make a network call`() {
//        runBlocking(Dispatchers.IO) {
//            every { persistenceManager.getProducts() } returns
//                    MutableLiveData<List<ProductEntity>>().also {
//                        it.value = dbResponse
//                    }
//
//            coEvery { api.getFlights() } returns Response.success(200, networkResponse)
//
//            every { rateLimiter.shouldFetch(any(), any(), any()) } returns true
//
//
//            val response = repository.getFlights(false)
//            val responseObserver = response.test()
//            val latch = CountDownLatch(2)
//            val observer = Observer<Result<List<ProductEntity>>> {
//                latch.countDown()
//            }
//            response.observeForever(observer)
//            latch.await(10, TimeUnit.SECONDS)
//            responseObserver
//                .assertHasValue()
//                .assertHistorySize(2)
//                .assertValue(Result.success(dbResponse))
//                .assertValueHistory(Result.loading(dbResponse), Result.success(dbResponse))
//        }
//    }
//
//    @Test
//    fun `no data from db and no network call`() {
//        runBlocking(Dispatchers.IO) {
//            every { persistenceManager.getProducts() } returns MutableLiveData<List<ProductEntity>>().also {
//                it.value = listOf()
//            }
//
//            coEvery { api.getFlights() } returns Response.success(200, networkResponse)
//
//            every { rateLimiter.shouldFetch(any(), any(), any()) } returns false
//
//            val response = repository.getFlights(false)
//            val responseObserver = response.test()
//            val latch = CountDownLatch(1)
//            val observer = Observer<Result<List<ProductEntity>>> {
//                latch.countDown()
//            }
//            response.observeForever(observer)
//            latch.await(10, TimeUnit.SECONDS)
//            responseObserver
//                .assertHasValue()
//                .assertHistorySize(1)
//                .assertValue(Result.success(listOf()))
//        }
//    }
//
//    @Test
//    fun `load data from db and no network call`() {
//        runBlocking(Dispatchers.IO) {
//            every {
//                persistenceManager.getProducts()
//            } returns MutableLiveData<List<ProductEntity>>().also {
//                it.value = dbResponse
//            }
//
//            coEvery {
//                api.getFlights()
//            } returns
//                    Response.success(200, networkResponse)
//
//            every { rateLimiter.shouldFetch(any(), any(), any()) } returns false
//
//            val response = repository.getFlights(false)
//            val responseObserver = response.test()
//            val latch = CountDownLatch(1)
//            val observer = Observer<Result<List<ProductEntity>>> {
//                latch.countDown()
//            }
//            response.observeForever(observer)
//            latch.await(10, TimeUnit.SECONDS)
//            responseObserver
//                .assertHasValue()
//                .assertHistorySize(1)
//                .assertValue(Result.success(dbResponse))
//        }
//    }
//
//
//    @Test
//    fun `load data from db and error in the network call`() {
//        runBlocking(Dispatchers.IO) {
//            every { persistenceManager.getProducts() } returns
//                    MutableLiveData<List<ProductEntity>>().also {
//                        it.value = dbResponse
//                    }
//
//            coEvery { api.getFlights() } returns Response.error(
//                500,
//                "{\"message\":\"some_value\"}".toResponseBody(
//                    "application/json".toMediaType()
//                )
//            )
//
//            every { rateLimiter.shouldFetch(any(), any(), any()) } returns true
//
//            val response = repository.getFlights(false)
//            val responseObserver = response.test()
//            val latch = CountDownLatch(2)
//            val observer = Observer<Result<List<ProductEntity>>> {
//                latch.countDown()
//            }
//            response.observeForever(observer)
//            latch.await(10, TimeUnit.SECONDS)
//            responseObserver
//                .assertHasValue()
//                .assertHistorySize(2)
//                .assertValue(Result.error("Response.error()", dbResponse))
//                .assertValueHistory(
//                    Result.loading(dbResponse),
//                    Result.error("Response.error()", dbResponse)
//                )
//        }
//    }
//
//    @Test
//    fun `load data from db and exception in the network call`() {
//        runBlocking(Dispatchers.IO) {
//            every {
//                persistenceManager.getProducts()
//            } returns MutableLiveData<List<ProductEntity>>().also {
//                it.value = dbResponse
//            }
//
//            coEvery { api.getFlights() } throws Exception()
//
//        }
//
//        every { rateLimiter.shouldFetch(any(), any(), any()) } returns true
//
//        val response = repository.getFlights(false)
//        val responseObserver = response.test()
//        val latch = CountDownLatch(2)
//        val observer =
//            Observer<Result<List<ProductEntity>>> {
//                latch.countDown()
//            }
//        response.observeForever(observer)
//        latch.await(10, TimeUnit.SECONDS)
//        responseObserver
//            .assertHasValue()
//            .assertHistorySize(2)
//            .assertValue(
//                Result.error(
//                    "error fetching from network",
//                    dbResponse
//                )
//            )
//            .assertValueHistory(
//                Result.loading(dbResponse),
//                Result.error(
//                    "error fetching from network",
//                    dbResponse
//                )
//            )
//    }
//
//
//    @Test
//    fun `force to make a network call even when shouldFetch returns false`() {
//        runBlocking(Dispatchers.IO) {
//            every {
//                persistenceManager.getProducts()
//            } returns MutableLiveData<List<ProductEntity>>().also {
//                it.value = dbResponse
//            }
//
//            coEvery { api.getFlights() } returns Response.success(200, networkResponse)
//
//            every { rateLimiter.shouldFetch(any(), any(), any()) } returns false
//
//            val response = repository.getFlights(
//                forceFetchInfo = true
//            )
//            val responseObserver = response.test()
//            val latch = CountDownLatch(2)
//            val observer =
//                Observer<Result<List<ProductEntity>>> {
//                    latch.countDown()
//                }
//            response.observeForever(observer)
//            latch.await(10, TimeUnit.SECONDS)
//            responseObserver
//                .assertHasValue()
//                .assertHistorySize(2)
//                .assertValue(
//                    Result.success(
//                        dbResponse
//                    )
//                )
//                .assertValueHistory(
//                    Result.loading(dbResponse),
//                    Result.success(dbResponse)
//                )
//        }
//    }
//
//    @Test
//    fun `verify item added to basket`() {
//        val code = "code"
//        runBlocking(Dispatchers.IO) {
//            repository.addProductToBasket(code)
//            coVerify(exactly = 1) {
//                persistenceManager.addProductToBasket(code)
//            }
//        }
//    }
//
//    @Test
//    fun `verify item removed from basket`() {
//        val code = "code"
//        runBlocking(Dispatchers.IO) {
//            repository.removeProductFromBasket(code)
//            coVerify(exactly = 1) {
//                persistenceManager.removeProductFromBasket(code)
//            }
//        }
//    }
//
//    @Test
//    fun `verify items in basket loaded`() {
//        runBlocking(Dispatchers.IO) {
//            repository.getProductsInBasket()
//            coVerify(exactly = 1) {
//                persistenceManager.getProductsInBasket()
//            }
//        }
//    }
//
//    @Test
//    fun `verify basket cleared`() {
//        runBlocking(Dispatchers.IO) {
//            repository.clearBasket()
//            coVerify(exactly = 1) {
//                persistenceManager.clearBasket()
//            }
//        }
//    }

}
