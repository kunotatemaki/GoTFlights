package com.rookia.gotflights.framework.persistence.entities.mapper

import com.rookia.gotflights.domain.network.model.ExchangeRate
import com.rookia.gotflights.framework.persistence.entities.ExchangeRateEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*


class ExchangeRateEntityMapperTest {

    @Test
    fun map() {
        val date = Date()
        val originalCurrency = "GBP"
        val targetCurrency = "EUR"
        val rate = 1.5
        val exchangeRate = ExchangeRate(targetCurrency, rate)
        val testEntity = ExchangeRateEntity(
            relation = ExchangeRateEntityMapper.getRelationName(
                from = originalCurrency,
                to = targetCurrency
            ),
            value = rate,
            date = date,
            to = targetCurrency,
            from = originalCurrency
        )

        val entity = ExchangeRateEntityMapper.map("GBP", exchangeRate, date)

        assertEquals(testEntity, entity)

    }

    @Test
    fun getRelationName() {
        assertEquals("GBP-EUR", ExchangeRateEntityMapper.getRelationName("GBP", "EUR"))
    }
}