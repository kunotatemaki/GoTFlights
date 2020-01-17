package com.rookia.gotflights.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rookia.gotflights.R
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.databinding.BindingComponent
import com.rookia.gotflights.databinding.FlightRowBinding
import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.utils.formatDecimalValue

// TODO: 17/01/2020 usar un recycler normal
class FlightsAdapter(
    private val bindingComponent: BindingComponent,
    private val resourcesManager: ResourcesManager
) :
    ListAdapter<Flight, FlightsAdapter.FlightViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<FlightRowBinding>(
                inflater,
                R.layout.flight_row,
                parent,
                false,
                bindingComponent
            )
        return FlightViewHolder(binding, resourcesManager)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        val flight: Flight? = getItem(position)

        holder.bindTo(flight)
    }

    class FlightViewHolder(
        private val binding: FlightRowBinding,
        private val resourcesManager: ResourcesManager
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindTo(flight: Flight?) {
            binding.flight = flight
            binding.resources = resourcesManager
            binding.price.text = flight?.getConvertedPriceFormatted()
            binding.outboundRow.legDirection.rotationY = 180f
            binding.executePendingBindings()
        }
    }


    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Flight>() {
            override fun areItemsTheSame(
                oldFlight: Flight,
                newFlight: Flight
            ) = oldFlight.inbound?.origin == newFlight.inbound?.origin &&
                    oldFlight.inbound?.destination == newFlight.inbound?.destination

            override fun areContentsTheSame(
                oldFlight: Flight,
                newFlight: Flight
            ) = oldFlight == newFlight
        }
    }


}