package com.rookia.gotflights.domain.model.mapper

interface BaseMapper<in A, out B> {
    fun map(type: A): B
}
 