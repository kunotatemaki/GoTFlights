package com.rookia.gotflights.utils

import java.util.concurrent.TimeUnit

class RateLimiter {

    /**
     * @param lastFetched: timestamp (ms) of the last time the info was fetched
     * @param timeout: max time to wait until the info is fetched again
     * @param timeUnit: the format in which #timeout is provided
     * @return true if the info need to be fetched again, false otherwise
     */
    @Synchronized
    fun shouldFetch(lastFetched: Long, timeout: Int, timeUnit: TimeUnit): Boolean {
        val timeoutFormatted: Long = timeUnit.toMillis(timeout.toLong())
        val now = now()
        if (lastFetched == 0L) {
            return true
        }
        return now - lastFetched > timeoutFormatted
    }

    private fun now(): Long {
        return System.currentTimeMillis()
    }

}