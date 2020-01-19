package com.rookia.gotflights


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rookia.gotflights.data.persistence.PersistenceManager
import com.rookia.gotflights.framework.persistence.PersistenceManagerImpl
import com.rookia.gotflights.framework.persistence.databases.AppDatabase
import com.rookia.gotflights.framework.persistence.entities.FooEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import testclasses.getItem

@Suppress("BlockingMethodInNonBlockingContext")
@RunWith(AndroidJUnit4::class)
class LocalDbTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var database: AppDatabase
    private lateinit var persistenceManager: PersistenceManager

    private val dbFoo1 = FooEntity(1, "name1", 1.0)
    private val dbFoo2 = FooEntity(2, "name2", 2.0)
    private val dbFoo3 = FooEntity(3, "name3", 3.0)

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
    fun testMoreThanOneItem() {
        runBlocking {
            persistenceManager.storeFoo(
                listOf(dbFoo1, dbFoo2, dbFoo3)
            )
            val items = persistenceManager.getFoo().getItem()

            assertEquals(3, items.size)
        }
    }

}