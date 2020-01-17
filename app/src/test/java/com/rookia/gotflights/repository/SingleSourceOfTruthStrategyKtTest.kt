package com.rookia.gotflights.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.jraska.livedata.test
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.framework.repository.resultLiveData
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@Suppress("UNCHECKED_CAST")
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class SingleSourceOfTruthStrategyKtTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    interface SingleSourceOfTruthTestClass {
        fun databaseQuery(): LiveData<List<Any>>
        suspend fun networkCall(): Result<Int>
        suspend fun saveCallResult(value: Any?)
    }

    private var singleSourceOfTruthTestClass: SingleSourceOfTruthTestClass = mockk()

    @Before
    fun setUp() {

        Dispatchers.setMain(mainThreadSurrogate)
        every { singleSourceOfTruthTestClass.databaseQuery() } returns MutableLiveData<Any>().also {
            it.postValue(
                listOf<Any>()
            )
        } as LiveData<List<Any>>
        coEvery { singleSourceOfTruthTestClass.saveCallResult(any()) } just Runs
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `don't call server if runNetworkCall is false`() {
        runBlocking(Dispatchers.IO) {
            val testLiveData = resultLiveData(
                { singleSourceOfTruthTestClass.databaseQuery() },
                { singleSourceOfTruthTestClass.networkCall() },
                { singleSourceOfTruthTestClass.saveCallResult(any()) },
                runNetworkCall = false
            )

            val testObserver = testLiveData.test()
            val latch = CountDownLatch(1)
            val observer = Observer<Result<List<Any>>> {
                latch.countDown()
            }
            testLiveData.observeForever(observer)
            latch.await(10, TimeUnit.SECONDS)
            testObserver
                .assertHasValue()
                .assertHistorySize(1)
                .assertValue(Result.success(listOf()))
        }
    }

    @Test
    fun `call server if runNetworkCall is true, with success response`() {
        coEvery { singleSourceOfTruthTestClass.networkCall() } returns Result.success(8)
        runBlocking(Dispatchers.IO) {
            val testLiveData = resultLiveData(
                { singleSourceOfTruthTestClass.databaseQuery() },
                { singleSourceOfTruthTestClass.networkCall() },
                { singleSourceOfTruthTestClass.saveCallResult(any()) },
                runNetworkCall = true
            )

            val testObserver = testLiveData.test()
            val latch = CountDownLatch(2)
            val observer = Observer<Result<List<Any>>> {
                latch.countDown()
            }
            testLiveData.observeForever(observer)
            latch.await(10, TimeUnit.SECONDS)
            testObserver
                .assertHasValue()
                .assertHistorySize(2)
                .assertValue(Result.success(listOf()))
                .assertValueHistory(Result.loading(listOf()), Result.success(listOf()))
        }
    }

    @Test
    fun `call server if runNetworkCall is true, with error response`() {
        val errorMessage = "error message"
        coEvery { singleSourceOfTruthTestClass.networkCall() } returns Result.error(errorMessage, 8)
        runBlocking(Dispatchers.IO) {
            val testLiveData = resultLiveData(
                { singleSourceOfTruthTestClass.databaseQuery() },
                { singleSourceOfTruthTestClass.networkCall() },
                { singleSourceOfTruthTestClass.saveCallResult(any()) },
                runNetworkCall = true
            )

            val testObserver = testLiveData.test()
            val latch = CountDownLatch(2)
            val observer = Observer<Result<List<Any>>> {
                latch.countDown()
            }
            testLiveData.observeForever(observer)
            latch.await(10, TimeUnit.SECONDS)
            testObserver
                .assertHasValue()
                .assertHistorySize(2)
                .assertValue(Result.error(errorMessage, listOf()))
                .assertValueHistory(Result.loading(listOf()), Result.error(errorMessage, listOf()))
        }
    }
}