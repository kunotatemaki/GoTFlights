package com.rookia.gotflights.utils

import com.rookia.gotflights.R
import com.rookia.gotflights.data.resources.ResourcesManager
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer


object DateUtils {

    fun parseStringDate(date: String?, time: String?): Date? {
        return try {
            date ?: return null
            time ?: return null
            val joined = "$date-$time"
            val dateFormat = SimpleDateFormat("MM/dd/yyyy-HH:mm", Locale.getDefault())
            dateFormat.parse(joined)
        } catch (e: Exception) {
            null
        }
    }

    fun elapsedTime(date1: Date, date2: Date, resourcesManager: ResourcesManager): String{
        val diff = date2.time - date1.time
        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        val date2WithoutDays = date2.time - days*24*3600*1000
        val diffWithoutDays = date2WithoutDays - date1.time
        val hours = TimeUnit.HOURS.convert(diffWithoutDays, TimeUnit.MILLISECONDS)
        val date2WithoutDaysAndHours = date2WithoutDays - hours*3600*1000
        val diffWithoutDaysAndHours = date2WithoutDaysAndHours - date1.time
        val minutes = TimeUnit.MINUTES.convert(diffWithoutDaysAndHours, TimeUnit.MILLISECONDS)
        var elapsed = ""
        if(days > 0){
           elapsed = elapsed + days.toString() + resourcesManager.getString(R.string.day)
        }
        if(hours > 0){
            if(elapsed.isNotBlank()) elapsed = "$elapsed, "
            elapsed = elapsed + hours.toString() + resourcesManager.getString(R.string.hour)
        }
        if(minutes > 0){
            if(elapsed.isNotBlank()) elapsed = "$elapsed, "
            elapsed = elapsed + minutes.toString() + resourcesManager.getString(R.string.minutes)
        }

        return elapsed
    }

    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) {
            return false
        }
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameDay(cal1, cal2)
    }

    fun isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
        if (cal1 == null || cal2 == null) {
            return false
        }
        return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun isToday(date1: Date?): Boolean{
        if (date1 == null) {
            return false
        }
        val today = Calendar.getInstance().apply {
            time = Date()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        return isSameDay(date1, today)
    }


}