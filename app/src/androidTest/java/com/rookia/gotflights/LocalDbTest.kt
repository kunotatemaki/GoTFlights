package com.rookia.gotflights


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.framework.persistence.PersistenceManagerImpl
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@Suppress("BlockingMethodInNonBlockingContext")
@RunWith(AndroidJUnit4::class)
class LocalDbTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val gbp = "GBP"
    private val usd = "USD"
    private val eur = "EUR"
    private val gbpToEur = 1.5
    private val usdToEur = 0.8
    private val date = Date()
    private lateinit var database: AppDatabase
    private lateinit var persistenceManager: PersistenceManager

    @Before
    @Throws(Exception::class)
    fun setUp() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build() // allowing main thread queries, just for testing
        persistenceManager = PersistenceManagerImpl(database)

    }

    @After
    @Throws(Exception::class)
    fun close() {
        database.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testTableEmpty() {
        runBlocking {
            val rate = persistenceManager.getExchangeRate(from = gbp, to = eur)
            assertNull(rate)
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun testGBP_and_noUSD() {
        runBlocking {
            persistenceManager.storeExchangeRate(gbp, ExchangeRate(eur, gbpToEur), date)
            val storedGBP = persistenceManager.getExchangeRate(gbp, eur)
            assertEquals(gbp, storedGBP!!.from)
            val storedUSD = persistenceManager.getExchangeRate(usd, eur)
            assertNull(storedUSD)
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun testGBP_and_USD() {
        runBlocking {
            persistenceManager.storeExchangeRate(gbp, ExchangeRate(eur, gbpToEur), date)
            persistenceManager.storeExchangeRate(usd, ExchangeRate(eur, usdToEur), date)
            val storedGBP = persistenceManager.getExchangeRate(gbp, eur)
            assertEquals(gbp, storedGBP!!.from)
            val storedUSD = persistenceManager.getExchangeRate(usd, eur)
            assertEquals(usd, storedUSD!!.from)
        }
    }

}