package com.rookia.gotflights.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rookia.gotflights.R
import com.rookia.gotflights.data.resources.ResourcesManager
import com.rookia.gotflights.databinding.BindingComponent
import com.rookia.gotflights.databinding.FlightRowBinding
import com.rookia.gotflights.domain.model.Flight

class FlightsAdapter(
    private val bindingComponent: BindingComponent,
    private val resourcesManager: ResourcesManager
) :
    RecyclerView.Adapter<FlightsAdapter.FlightViewHolder>() {

    private var list: List<Flight> = listOf()

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
        val flight: Flight? = list[position]
        holder.bindTo(flight)
    }

    override fun getItemCount(): Int = list.size

    fun submitList(list: List<Flight>?){
        list?.let {
            this.list = list
            notifyDataSetChanged()
        }
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

}