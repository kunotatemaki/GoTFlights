package com.rookia.gotflights.domain.model.mapper

import com.rookia.gotflights.domain.model.Product
import com.rookia.gotflights.domain.network.AppApi
import com.rookia.gotflights.framework.persistence.entities.ProductEntity


object ApiToEntityMapper : BaseMapper<AppApi.Item, ProductEntity> {
    override fun map(type: AppApi.Item): ProductEntity =
        ProductEntity(code = type.code, name = type.name, price = type.price)
}

object EntityToProduct : BaseMapper<ProductEntity, Product> {
    override fun map(type: ProductEntity): Product =
        Product(code = type.code, name = type.name, price = type.price)
}