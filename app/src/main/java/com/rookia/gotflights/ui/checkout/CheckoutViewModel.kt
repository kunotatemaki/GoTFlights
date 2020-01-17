package com.rookia.gotflights.ui.checkout

import androidx.lifecycle.*
import com.rookia.gotflights.domain.model.Checkout
import com.rookia.gotflights.domain.model.mapper.EntityToProduct
import com.rookia.gotflights.usecases.ClearBasketUseCase
import com.rookia.gotflights.usecases.GetFlightsUseCase
import com.rookia.gotflights.framework.persistence.entities.BasketEntity
import com.rookia.gotflights.framework.persistence.entities.ProductEntity
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.domain.network.Flight
import com.rookia.gotflights.utils.calculateDiscounts
import kotlinx.coroutines.launch

open class CheckoutViewModel constructor(
    getFlightsUseCase: GetFlightsUseCase,
    private val clearBasketUseCase: ClearBasketUseCase,
    private val resourcesManager: ResourcesManager
) : ViewModel() {

    private var checkoutInfo: MediatorLiveData<Checkout> = MediatorLiveData()
//    private var productsFromCache: LiveData<List<Flight>> =
//        getFlightsUseCase.getProductsFromCache()
//    private var productsIbBasket: LiveData<List<BasketEntity>> =
//        getFlightsUseCase.getProductsInBasket()


    init {

//        checkoutInfo.apply {
//            addSource(productsFromCache) {
//                getCheckoutInfo()
//            }
//            addSource(productsIbBasket) {
//                getCheckoutInfo()
//            }
//        }
    }

    private fun getCheckoutInfo() {
//        val list = productsFromCache.value?.map { product -> EntityToProduct.map(product) }
//        //add info about how many times the item has been added to the basket
//        list?.forEach { product ->
//            product.timesInBasket =
//                productsIbBasket.value?.firstOrNull { itemInBasket -> itemInBasket.code == product.code }?.selections
//                    ?: 0
//        }
//        //calculate discounts
//        val discounts = calculateDiscounts(products = list, resourcesManager = resourcesManager)
//        checkoutInfo.value = Checkout(products = list?.filter { it.timesInBasket > 0 } ?: listOf(),
//            discounts = discounts.filter { it.savedMoney > 0 })
    }

//    fun getCheckoutAsObservable(): LiveData<Checkout> =
//        Transformations.distinctUntilChanged(checkoutInfo)



}
