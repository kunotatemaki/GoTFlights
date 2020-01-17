package com.rookia.gotflights


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.domain.model.FlightsCache
import com.rookia.gotflights.framework.persistence.PersistenceManagerImpl
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import com.rookia.gotflights.framework.persistence.entities.FooEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import testclasses.getFlightFromNetwork
import testclasses.getItem

@Suppress("BlockingMethodInNonBlockingContext")
@RunWith(AndroidJUnit4::class)
class LocalCacheTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: AppDatabase
    private lateinit var persistenceManager: PersistenceManager
    private var flightsCache: FlightsCache? = null

    private val dbFoo1 = FooEntity(1, "name1", 1.0)
    private val dbFoo2 = FooEntity(2, "name2", 2.0)
    private val dbFoo3 = FooEntity(3, "name3", 3.0)
    private val networkFlight1 = getFlightFromNetwork("Valladolid", "Zamora", 1.toBigDecimal())
    private val networkFlight2 = getFlightFromNetwork("Valencia", "Madrid", 11.toBigDecimal())
    private val networkFlight3 = getFlightFromNetwork("Barcelona", "Sevilla", 31.toBigDecimal())

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

        flightsCache = FlightsCache()

    }

    @After
    @Throws(Exception::class)
    fun close() {
        database.close()
        flightsCache = null
    }

    @Test
    @Throws(InterruptedException::class)
    fun testFooTableEmpty() {
        val foo = persistenceManager.getFoo()
        assertTrue(foo.getItem().isEmpty())

    }

    @Test
    @Throws(InterruptedException::class)
    fun testOneFooItem() {
        runBlocking {
            persistenceManager.storeFoo(listOf(dbFoo1))
            val items = persistenceManager.getFoo().getItem()
            assertEquals(1, items.size)
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun testMoreThanOneProduct() {
        runBlocking {
            persistenceManager.storeFoo(
                listOf(dbFoo1, dbFoo2, dbFoo3)
            )
            val items = persistenceManager.getFoo().getItem()

            assertEquals(3, items.size)
        }
    }

    @Test
    @Throws(InterruptedException::class)
    fun testCacheEmpty() {
        val flights = flightsCache!!.getListOfFlightsInCache().getItem()
        assertTrue(flights.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDataInCache() {
        val flightsToStore = listOf(networkFlight1, networkFlight2, networkFlight3)
        flightsCache!!.saveListOfFlightsInCache(flightsToStore)
        val flights = flightsCache!!.getListOfFlightsInCache().getItem()
        assertEquals(3, flights.size)
    }



}