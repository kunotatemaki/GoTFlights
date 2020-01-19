package com.rookia.gotflights.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import com.appyvet.materialrangebar.RangeBar
import com.appyvet.materialrangebar.RangeBar.OnRangeBarChangeListener
import com.rookia.gotflights.R
import com.rookia.gotflights.databinding.MainFragmentBinding
import com.rookia.gotflights.di.injectViewModel
import com.rookia.gotflights.domain.vo.Result
import com.rookia.gotflights.ui.common.BaseFragment
import com.rookia.gotflights.utils.RangeBarValues
import java.lang.ref.WeakReference
import kotlin.math.abs


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
        binding.flightsList.apply {
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

        binding.rangeBar.setOnRangeBarChangeListener(object : OnRangeBarChangeListener {
            override fun onRangeChangeListener(
                rangeBar: RangeBar,
                leftPinIndex: Int,
                rightPinIndex: Int,
                leftPinValue: String,
                rightPinValue: String
            ) {
                with(binding.rangeBar) {
                    binding.startValue.text =
                        RangeBarValues.getPinValue(this.tickStart, this.tickInterval, leftPinIndex)
                            .toString()
                    binding.endValue.text =
                        RangeBarValues.getPinValue(
                            this.tickStart,
                            this.tickInterval,
                            abs(rightPinIndex)
                        ).toString()
                }
            }

            override fun onTouchEnded(rangeBar: RangeBar) {}
            override fun onTouchStarted(rangeBar: RangeBar) {}
        })
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

        viewModel.filterShown.observe(viewLifecycleOwner, Observer {
            if(it== true) {
                binding.filtersContainer.visibility = View.VISIBLE
            } else {
                binding.filtersContainer.visibility = View.GONE
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.checkout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                showFilterValues()
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

    private fun showFilterValues() {
        if (viewModel.canShowFilter().not()) {
            activity?.let {
                viewUtils.showAlertDialog(
                    activity = WeakReference(it),
                    allowCancelWhenTouchingOutside = false,
                    positiveButton = resourcesManager.getString(R.string.accept),
                    message = resourcesManager.getString(R.string.no_values)
                )
            }
        } else {
            val min = RangeBarValues.getLowerValue(viewModel.minPrice ?: return)
            val max = RangeBarValues.getHigherValue(viewModel.maxPrice ?: return)
            val interval = RangeBarValues.getInterval(max, min)
            binding.rangeBar.tickStart = min.toFloat()
            binding.rangeBar.tickEnd = max.toFloat()
            binding.rangeBar.setTickInterval(interval.toFloat())
            viewModel.showFilter()
        }
    }

    private fun applyFilters() {
        viewModel.hideFilter()
        with(binding.rangeBar) {
            val minVal =
                RangeBarValues.getPinValue(this.tickStart, this.tickInterval, this.leftIndex)
            val maxVal =
                RangeBarValues.getPinValue(this.tickStart, this.tickInterval, this.rightIndex)
            viewModel.filterFlights(minVal.toFloat(), maxVal.toFloat() )
        }
    }

}
