package com.rookia.gotflights.domain

import com.rookia.gotflights.R
import com.rookia.gotflights.domain.model.Product
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.utils.cfoDiscount
import com.rookia.gotflights.utils.marketingDiscount
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test


class DiscountsHelperKtTest {

    private val pen = Product("VOUCHER", "Lana Voucher", 5.0)
    private val tshirt = Product("TSHIRT", "Lana T-Shirt", 20.0)
    private val mug = Product("MUG", "Lana Coffee Mug", 7.5)
    private val products = listOf(pen, tshirt, mug)

    private val marketingDescription = "marketing description"
    private val cfoDescription = "cfo description"

    @MockK
    lateinit var resourcesManager: ResourcesManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        every { resourcesManager.getString(R.string.marketing_discount_description) } returns marketingDescription

        every { resourcesManager.getString(R.string.cfo_discount_description) } returns cfoDescription

    }

    @Test
    fun marketingDiscountOdd() {
        products.first { it.code == "VOUCHER" }.timesInBasket = 5
        val discount = marketingDiscount(products, resourcesManager)
        assertNotNull(discount)
        assertEquals(discount.description, marketingDescription)
        assertEquals(discount.savedMoney, 10.0, 0.009)
    }

    @Test
    fun marketingDiscountEven() {
        products.first { it.code == "VOUCHER" }.timesInBasket = 4
        val discount = marketingDiscount(products, resourcesManager)
        assertNotNull(discount)
        assertEquals(discount.description, marketingDescription)
        assertEquals(discount.savedMoney, 10.0, 0.009)
    }

    @Test
    fun marketingDiscountLessThan2() {
        products.first { it.code == "VOUCHER" }.timesInBasket = 1
        val discount = marketingDiscount(products, resourcesManager)
        assertNotNull(discount)
        assertEquals(discount.description, marketingDescription)
        assertEquals(discount.savedMoney, 0.0, 0.009)
    }

    @Test
    fun marketingDiscountEmptyList() {
        products.first { it.code == "VOUCHER" }.timesInBasket = 0
        val discount = marketingDiscount(products, resourcesManager)
        assertNotNull(discount)
        assertEquals(discount.description, marketingDescription)
        assertEquals(discount.savedMoney, 0.0, 0.009)
    }

    @Test
    fun cfoDiscount3Tshirts() {
        products.first { it.code == "TSHIRT" }.timesInBasket = 3
        val discount = cfoDiscount(products, resourcesManager)
        assertNotNull(discount)
        assertEquals(discount.description, cfoDescription)
        assertEquals(discount.savedMoney, 3.0, 0.009)
    }

    @Test
    fun cfoDiscountMoreThan3Tshirts() {
        products.first { it.code == "TSHIRT" }.timesInBasket = 7
        val discount = cfoDiscount(products, resourcesManager)
        assertNotNull(discount)
        assertEquals(discount.description, cfoDescription)
        assertEquals(discount.savedMoney, 7.0, 0.009)
    }

    @Test
    fun cfoDiscount2Tshirts() {
        products.first { it.code == "TSHIRT" }.timesInBasket = 2
        val discount = cfoDiscount(products, resourcesManager)
        assertNotNull(discount)
        assertEquals(discount.description, cfoDescription)
        assertEquals(discount.savedMoney, 0.0, 0.009)
    }

    @Test
    fun cfoDiscount0Tshirts() {
        products.first { it.code == "TSHIRT" }.timesInBasket = 0
        val discount = cfoDiscount(products, resourcesManager)
        assertNotNull(discount)
        assertEquals(discount.description, cfoDescription)
        assertEquals(discount.savedMoney, 0.0, 0.009)
    }


}