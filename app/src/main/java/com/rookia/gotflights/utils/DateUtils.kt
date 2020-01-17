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

}