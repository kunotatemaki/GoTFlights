package com.rookia.gotflights.utils

import com.rookia.gotflights.R
import com.rookia.gotflights.data.resources.ResourcesManager
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.*

class DateUtilsTest {

    private val resourcesManager: ResourcesManager = mockk()
    private lateinit var today: Date
    private val now = Date()

    @Before
    fun setup(){
        every { resourcesManager.getString(R.string.day) } returns "d"
        every { resourcesManager.getString(R.string.hour) } returns "h"
        every { resourcesManager.getString(R.string.minutes) } returns "min"
        today = Calendar.getInstance().apply {
            time = now
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }

    @Test
    fun parseStringDate() {

        val date = DateUtils.parseStringDate("11/18/2275", "17:12")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date!!
        assertEquals(2275, calendar.get(Calendar.YEAR))
        assertEquals(11 - 1, calendar.get(Calendar.MONTH)) // month is zero-indexed
        assertEquals(18, calendar.get(Calendar.DAY_OF_MONTH))
        assertEquals(17, calendar.get(Calendar.HOUR_OF_DAY))
        assertEquals(12, calendar.get(Calendar.MINUTE))

    }

    @Test
    fun elapsedTime_ddHHmm() {
        val date1 = DateUtils.parseStringDate("11/18/2275", "17:12")
        val date2 = DateUtils.parseStringDate("11/25/2275", "18:15")
        val elapsed = DateUtils.elapsedTime(date1!!, date2!!, resourcesManager)
        assertEquals("7d, 1h, 3min", elapsed)
    }

    @Test
    fun elapsedTime_dd() {
        val date1 = DateUtils.parseStringDate("11/08/2275", "17:12")
        val date2 = DateUtils.parseStringDate("11/25/2275", "17:12")
        val elapsed = DateUtils.elapsedTime(date1!!, date2!!, resourcesManager)
        assertEquals("17d", elapsed)
    }

    @Test
    fun elapsedTime_HH() {
        val date1 = DateUtils.parseStringDate("11/18/2275", "17:12")
        val date2 = DateUtils.parseStringDate("11/18/2275", "19:12")
        val elapsed = DateUtils.elapsedTime(date1!!, date2!!, resourcesManager)
        assertEquals("2h", elapsed)
    }

    @Test
    fun elapsedTime_mm() {
        val date1 = DateUtils.parseStringDate("11/18/2275", "17:12")
        val date2 = DateUtils.parseStringDate("11/18/2275", "17:15")
        val elapsed = DateUtils.elapsedTime(date1!!, date2!!, resourcesManager)
        assertEquals("3min", elapsed)
    }

    @Test
    fun elapsedTime_ddHH() {
        val date1 = DateUtils.parseStringDate("11/18/2275", "17:12")
        val date2 = DateUtils.parseStringDate("11/25/2275", "18:12")
        val elapsed = DateUtils.elapsedTime(date1!!, date2!!, resourcesManager)
        assertEquals("7d, 1h", elapsed)
    }

    @Test
    fun elapsedTime_ddmm() {
        val date1 = DateUtils.parseStringDate("11/18/2275", "17:12")
        val date2 = DateUtils.parseStringDate("11/25/2275", "17:15")
        val elapsed = DateUtils.elapsedTime(date1!!, date2!!, resourcesManager)
        assertEquals("7d, 3min", elapsed)
    }

    @Test
    fun elapsedTime_HHmm() {
        val date1 = DateUtils.parseStringDate("11/18/2275", "17:12")
        val date2 = DateUtils.parseStringDate("11/18/2275", "18:15")
        val elapsed = DateUtils.elapsedTime(date1!!, date2!!, resourcesManager)
        assertEquals("1h, 3min", elapsed)
    }

    @Test
    fun testIsSame() {
        assert(DateUtils.isSameDay(now, today))
    }
}