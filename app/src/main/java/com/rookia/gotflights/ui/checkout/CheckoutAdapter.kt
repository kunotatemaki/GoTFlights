package com.rookia.gotflights.ui.checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rookia.gotflights.R
import com.rookia.gotflights.databinding.BindingComponent
import com.rookia.gotflights.databinding.CheckoutActionsBinding
import com.rookia.gotflights.databinding.CheckoutItemBinding
import com.rookia.gotflights.databinding.CheckoutTitleBinding
import com.rookia.gotflights.domain.model.Checkout
import com.rookia.gotflights.data.resources.ResourcesManager


class CheckoutAdapter(
    private var checkout: Checkout,
    private val resourcesManager: ResourcesManager,
    private val bindingComponent: BindingComponent,
    private val checkoutBasketInteractions: CheckoutBasketInteractions
) :
    RecyclerView.Adapter<CheckoutViewHolder>() {

    enum class CheckoutType(val value: Int) {
        TITLE(0),
        PRODUCT(1),
        DISCOUNT(2),
        SAVING(3),
        TOTAL(4),
        ACTIONS(5)
    }

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> CheckoutType.TITLE.value
            in 1 until (1 + checkout.products.size) -> CheckoutType.PRODUCT.value
            1 + checkout.products.size -> CheckoutType.TITLE.value
            in 2 + checkout.products.size until (2 + checkout.products.size + getDiscountsSize()) -> CheckoutType.DISCOUNT.value
            2 + checkout.products.size + getDiscountsSize() -> CheckoutType.TOTAL.value
            3 + checkout.products.size + getDiscountsSize() -> CheckoutType.SAVING.value
            else -> CheckoutType.ACTIONS.value
        }

    private fun getDiscountsSize(): Int =
        if (checkout.discounts.isEmpty()) 1 else checkout.discounts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CheckoutType.PRODUCT.value -> {
                val binding =
                    DataBindingUtil.inflate<CheckoutItemBinding>(
                        inflater,
                        R.layout.checkout_item,
                        parent,
                        false,
                        bindingComponent
                    )
                ProductCheckoutViewHolder(binding, resourcesManager)
            }
            CheckoutType.DISCOUNT.value -> {
                val binding =
                    DataBindingUtil.inflate<CheckoutItemBinding>(
                        inflater,
                        R.layout.checkout_item,
                        parent,
                        false,
                        bindingComponent
                    )
                DiscountCheckoutViewHolder(binding, resourcesManager)
            }
            CheckoutType.SAVING.value -> {
                val binding =
                    DataBindingUtil.inflate<CheckoutTitleBinding>(
                        inflater,
                        R.layout.checkout_title,
                        parent,
                        false,
                        bindingComponent
                    )
                SavingCheckoutViewHolder(binding, resourcesManager)
            }
            CheckoutType.TOTAL.value -> {
                val binding =
                    DataBindingUtil.inflate<CheckoutTitleBinding>(
                        inflater,
                        R.layout.checkout_title,
                        parent,
                        false,
                        bindingComponent
                    )
                TotalCheckoutViewHolder(binding, resourcesManager)
            }
            CheckoutType.ACTIONS.value -> {
                val binding =
                    DataBindingUtil.inflate<CheckoutActionsBinding>(
                        inflater,
                        R.layout.checkout_actions,
                        parent,
                        false,
                        bindingComponent
                    )
                ActionCheckoutViewHolder(binding, resourcesManager)
            }
            else -> {
                val binding =
                    DataBindingUtil.inflate<CheckoutTitleBinding>(
                        inflater,
                        R.layout.checkout_title,
                        parent,
                        false,
                        bindingComponent
                    )
                TitleCheckoutViewHolder(binding, resourcesManager)
            }
        }
    }

    // productTitle + products + discountsTitle + (discounts or no_discount) + saving + pay + action_buttons
    override fun getItemCount(): Int {
        val discountsSize = if (checkout.discounts.isEmpty()) 1 else checkout.discounts.size
        return 1 + checkout.products.size + 1 + discountsSize + 1 + 1 + 1
    }


    override fun onBindViewHolder(holder: CheckoutViewHolder, position: Int) {
        when (holder) {
            is TitleCheckoutViewHolder -> {
                val title = when (position) {
                    0 -> {
                        resourcesManager.getString(R.string.products_title)
                    }
                    else -> {
                        resourcesManager.getString(R.string.discounts_title)
                    }
                }
                holder.bind(title)
            }
            is SavingCheckoutViewHolder -> {
                holder.bind(resourcesManager.getString(R.string.money_saved), checkout)
            }
            is TotalCheckoutViewHolder -> {
                holder.bind(resourcesManager.getString(R.string.money_to_pay), checkout)
            }
            is DiscountCheckoutViewHolder -> {
                if (checkout.discounts.isEmpty()) {
                    holder.bind(null)
                } else {
                    val discount = checkout.discounts[position - 2 - checkout.products.size]
                    holder.bind(discount)
                }
            }
            is ProductCheckoutViewHolder -> {
                val product = checkout.products[position - 1]
                holder.bind(product)
            }
            is ActionCheckoutViewHolder -> {
                holder.bind(checkoutBasketInteractions)
            }
        }
    }


}