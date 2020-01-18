package com.rookia.gotflights.ui.main

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
import com.rookia.gotflights.ui.common.BaseFragment
import com.rookia.gotflights.domain.vo.Result


class FlightsFragment : BaseFragment() {

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
        adapter = FlightsAdapter(bindingComponent, resourcesManager)
        binding.productList.apply {
            adapter = this@FlightsFragment.adapter
            val itemDecor = DividerItemDecoration(context, VERTICAL)
            addItemDecoration(itemDecor)
        }
        binding.loading.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.filtersApply.setOnClickListener {
            applyFilters()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                binding.filtersContainer.visibility = View.VISIBLE
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

    private fun applyFilters(){
        binding.filtersContainer.visibility = View.GONE
        viewModel.filterFlights()
    }

}
