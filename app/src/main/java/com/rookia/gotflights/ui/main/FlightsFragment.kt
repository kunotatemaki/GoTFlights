package com.rookia.gotflights.ui.main

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.rookia.gotflights.R
import com.rookia.gotflights.databinding.MainFragmentBinding
import com.rookia.gotflights.di.injectViewModel
import com.rookia.gotflights.ui.checkout.CheckoutDialogFragment
import com.rookia.gotflights.ui.common.BaseFragment
import com.rookia.gotflights.ui.customviews.CountDrawable
import com.rookia.gotflights.domain.vo.Result


class FlightsFragment : BaseFragment(), ProductsBasketInteractions {

    private lateinit var binding: MainFragmentBinding

    private lateinit var viewModel: FlightsViewModel

    private lateinit var adapter: FlightsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false,
            bindingComponent
        )
        adapter = FlightsAdapter(bindingComponent, resourcesManager, this)
        binding.productList.apply {
            adapter = this@FlightsFragment.adapter
            val itemDecor = DividerItemDecoration(context, VERTICAL)
            addItemDecoration(itemDecor)
        }
        binding.loading.setOnRefreshListener {
            viewModel.refresh()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = injectViewModel(viewModelFactory)
        viewModel.flights
            .observe(viewLifecycleOwner, Observer {
                it?.let {
                    when (it.status) {
                        Result.Status.SUCCESS -> {
                            hideLoading()
                            adapter.submitList(it.data)
                        }
                        Result.Status.ERROR -> {
                            hideLoading()
                            showError(it.message)
                            it.data?.let { list -> adapter.submitList(list) }
                        }
                        Result.Status.LOADING -> {
                            showLoading()
                            it.data?.let { list -> adapter.submitList(list) }
                        }
                    }
                    activity?.invalidateOptionsMenu()
                }
            })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.checkout_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        context?.let {
//            setCount(it, viewModel.getNumberOfSelectedItems().toString(), menu)
        }
    }

    private fun setCount(context: Context, count: String, menu: Menu) {
        val menuItem: MenuItem = menu.findItem(R.id.checkout_item)
        val icon = menuItem.icon as LayerDrawable
        val badge: CountDrawable
        // Reuse drawable if possible
        val reuse =
            icon.findDrawableByLayerId(R.id.ic_product_count)
        badge = if (reuse != null && reuse is CountDrawable) {
            reuse
        } else {
            CountDrawable(context)
        }
        badge.setCount(count)
        icon.mutate()
        icon.setDrawableByLayerId(R.id.ic_product_count, badge)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.checkout_item -> {
//                if (viewModel.getNumberOfSelectedItems() > 0) {
//                    showCheckoutScreen()
//                } else {
//                    activity?.let {
//                        viewUtils.showAlertDialog(
//                            activity = WeakReference(it),
//                            allowCancelWhenTouchingOutside = false,
//                            message = resourcesManager.getString(R.string.select_items),
//                            positiveButton = resourcesManager.getString(R.string.accept)
//                        )
//                    }
//                }
            }
            R.id.delete_basket_item -> {
//                activity?.let {
//                    viewUtils.showAlertDialog(
//                        activity = WeakReference(it),
//                        allowCancelWhenTouchingOutside = false,
//                        title = resourcesManager.getString(R.string.warning),
//                        message = resourcesManager.getString(R.string.delete_basket_confirmation),
//                        positiveButton = resourcesManager.getString(R.string.accept),
//                        callbackPositive = { viewModel.clearBasket() },
//                        negativeButton = resourcesManager.getString(R.string.cancel)
//                    )
//                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading() {
        binding.loading.isRefreshing = true
    }

    private fun hideLoading() {
        binding.loading.isRefreshing = false
    }

    private fun showError(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun addProductToBasket(code: String?) {
//        code?.let { viewModel.addProductToBasket(code) }
    }

    override fun removeProductToBasket(code: String?) {
//        code?.let { viewModel.removeProductToBasket(code) }
    }

    override fun clearBasket() {
//        viewModel.clearBasket()
    }

    private fun showCheckoutScreen() {
        activity?.supportFragmentManager?.let {
            CheckoutDialogFragment.newInstance().show(it, "checkout")
        }
    }
}
