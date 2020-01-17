package com.rookia.gotflights.utils

import com.rookia.gotflights.R
import com.rookia.gotflights.domain.model.Discount
import com.rookia.gotflights.domain.model.Product
import com.rookia.gotflights.data.resources.ResourcesManager

fun calculateDiscounts(
    products: List<Product>?,
    resourcesManager: ResourcesManager
): List<Discount> =
    listOf(
        marketingDiscount(products, resourcesManager),
        cfoDiscount(products, resourcesManager)
    )

// region discount per companies
/**
 * 2-for-1 on `PEN` items -> half of the items are free.
 *  If number of items is odd, half are free: 4 items -> 2 free
 *  If number of items is even, half (rounded down) are free: 5 items -> 2.5 half -> 2 free
 */
fun marketingDiscount(products: List<Product>?, resourcesManager: ResourcesManager): Discount {
    val description = resourcesManager.getString(R.string.marketing_discount_description)
    val penItem = products?.firstOrNull { it.code == "VOUCHER" } ?: return Discount(description, 0.0)
    val freeItems: Int = penItem.timesInBasket / 2
    return Discount(description, freeItems * penItem.price)
}

/**
 * 3 or more discount on `TSHIRT` items -> 19€.
 */
fun cfoDiscount(products: List<Product>?, resourcesManager: ResourcesManager): Discount {
    val description = resourcesManager.getString(R.string.cfo_discount_description)
    val shirtItem =
        products?.firstOrNull { it.code == "TSHIRT" } ?: return Discount(description, 0.0)
    val savingPerItem = if (shirtItem.timesInBasket < 3 || shirtItem.price <= 19.0) {
        0.0
    } else {
        shirtItem.price - 19.0
    }
    return Discount(description, shirtItem.timesInBasket * savingPerItem)
}

//endregion