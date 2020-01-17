package com.rookia.gotflights.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.rookia.gotflights.domain.model.Product
import com.rookia.gotflights.domain.model.mapper.EntityToProduct
import com.rookia.gotflights.domain.network.Flight
import com.rookia.gotflights.usecases.AddProductToBasketUseCase
import com.rookia.gotflights.usecases.ClearBasketUseCase
import com.rookia.gotflights.usecases.GetFlightsUseCase
import com.rookia.gotflights.usecases.RemoveProductFromBasketUseCase
import com.rookia.gotflights.framework.persistence.entities.ProductEntity
import com.rookia.gotflights.domain.vo.Result
import kotlinx.coroutines.launch

open class FlightsViewModel constructor(
    private val getFlightsUseCase: GetFlightsUseCase,
    private val addProductToBasketUseCase: AddProductToBasketUseCase,
    private val removeProductFromBasketUseCase: RemoveProductFromBasketUseCase,
    private val clearBasketUseCase: ClearBasketUseCase
) : ViewModel() {

    val flights: LiveData<Result<List<Flight>>>
//    get() = Transformations.distinctUntilChanged(field)

    @VisibleForTesting
    val fetchTrigger = MutableLiveData<Long>()

    init {
        flights = fetchTrigger.switchMap {
            getFlightsUseCase.getFlights()
        }
        fetchTrigger.value = 0
    }

    private fun getProductsWithBasketInfo() {
//        val list = productsFromDb.value?.data?.map { product -> EntityToProduct.map(product) }
//        //add info about how many times the item has been added to the basket
//        list?.forEach { product ->
//            product.timesInBasket =
//                productsIbBasket.value?.firstOrNull { itemInBasket -> itemInBasket.code == product.code }?.selections
//                    ?: 0
//        }
//
//        val result = when (productsFromDb.value?.status) {
//            Result.Status.SUCCESS -> Result.success(list)
//            Result.Status.LOADING -> Result.loading(list)
//            Result.Status.ERROR -> Result.error(productsFromDb.value?.message, list)
//            else -> null
//        }
//        flights.value = result
    }

    fun refresh() {
        fetchTrigger.value = System.currentTimeMillis()
    }



}
