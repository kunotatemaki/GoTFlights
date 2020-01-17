package com.rookia.gotflights.ui.checkout

import android.annotation.SuppressLint
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.rookia.gotflights.R
import com.rookia.gotflights.databinding.CheckoutActionsBinding
import com.rookia.gotflights.databinding.CheckoutItemBinding
import com.rookia.gotflights.databinding.CheckoutTitleBinding
import com.rookia.gotflights.domain.model.Checkout
import com.rookia.gotflights.domain.model.Discount
import com.rookia.gotflights.domain.model.Product
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.utils.formatDecimalValue


sealed class CheckoutViewHolder(
    binding: ViewDataBinding,
    protected val resourcesManager: ResourcesManager
) : RecyclerView.ViewHolder(binding.root)

class TitleCheckoutViewHolder(
    private val binding: CheckoutTitleBinding,
    resourcesManager: ResourcesManager
) : CheckoutViewHolder(binding, resourcesManager) {
    @SuppressLint("SetTextI18n")
    fun bind(title: String) {
        binding.title = title
        binding.showPrice = false
    }
}

class SavingCheckoutViewHolder(
    private val binding: CheckoutTitleBinding,
    resourcesManager: ResourcesManager
) : CheckoutViewHolder(binding, resourcesManager) {
    @SuppressLint("SetTextI18n")
    fun bind(title: String, checkout: Checkout) {
        binding.title = title
        binding.showPrice = true
//        val price = formatDecimalValue(checkout.discounts.sumByDouble { it.savedMoney })
//        binding.checkoutTitlePrice.text = "$price€"
    }
}

class TotalCheckoutViewHolder(
    private val binding: CheckoutTitleBinding,
    resourcesManager: ResourcesManager
) : CheckoutViewHolder(binding, resourcesManager) {
    @SuppressLint("SetTextI18n")
    fun bind(title: String, checkout: Checkout) {
        binding.title = title
        binding.showPrice = true
//        val price = formatDecimalValue(
//            checkout.products.sumByDouble { it.price * it.timesInBasket } - checkout.discounts.sumByDouble { it.savedMoney }
//        )
//        binding.checkoutTitlePrice.text = "$price€"
    }
}

class ProductCheckoutViewHolder(
    private val binding: CheckoutItemBinding,
    resourcesManager: ResourcesManager
) : CheckoutViewHolder(binding, resourcesManager) {
    @SuppressLint("SetTextI18n")
    fun bind(product: Product) {
        val title = resourcesManager.getString(R.string.product_checkout)
        binding.title = String.format(title, product.timesInBasket, product.name)
        binding.showPrice = true
//        binding.checkoutItemPrice.text = "${formatDecimalValue(product.price * product.timesInBasket)}€"
    }
}

class DiscountCheckoutViewHolder(
    private val binding: CheckoutItemBinding,
    resourcesManager: ResourcesManager
) : CheckoutViewHolder(binding, resourcesManager) {
    @SuppressLint("SetTextI18n")
    fun bind(discount: Discount?) {
        if (discount == null) {
            binding.title = resourcesManager.getString(R.string.discount_checkout_empty)
            binding.showPrice = false
        } else {
            binding.title = discount.description
            binding.showPrice = true
//            binding.checkoutItemPrice.text = "${formatDecimalValue(discount.savedMoney)}€"
        }
    }

}

class ActionCheckoutViewHolder(
    private val binding: CheckoutActionsBinding,
    resourcesManager: ResourcesManager
) : CheckoutViewHolder(binding, resourcesManager) {
    fun bind(checkoutBasketInteractions: CheckoutBasketInteractions) {
        binding.cancelAction.setOnClickListener {
            checkoutBasketInteractions.cancelCheckout()
        }
        binding.checkoutAction.setOnClickListener {
            checkoutBasketInteractions.checkout()
        }
    }

}
