package com.rookia.gotflights.utils

interface BaseMapper<in A, out B> {
    fun map(type: A): B
}
 