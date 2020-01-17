package com.rookia.gotflights.domain.model.mapper

import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.model.Inbound
import com.rookia.gotflights.domain.model.Outbound
import com.rookia.gotflights.domain.network.model.FlightApiResponse
import com.rookia.gotflights.utils.BaseMapper


//todo convertir aquí a flight distinto, metiendo el tiempo total en días, horas...
object ApiToCacheMapper : BaseMapper<FlightApiResponse, Flight> {
    override fun map(type: FlightApiResponse): Flight =
        Flight(
            currency = type.currency,
            price = type.price,
            inbound = Inbound(
                airline = type.inbound?.airline,
                airlineImage = type.inbound?.airlineImage,
                arrivalDate = type.inbound?.arrivalDate,
                arrivalTime = type.inbound?.arrivalTime,
                departureTime = type.inbound?.arrivalTime,
                departureDate = type.inbound?.arrivalDate,
                origin = type.inbound?.origin,
                destination = type.inbound?.destination,
                elapsedTime = ""
            ),
            outbound = Outbound(
                airline = type.inbound?.airline,
                airlineImage = type.inbound?.airlineImage,
                arrivalDate = type.inbound?.arrivalDate,
                arrivalTime = type.inbound?.arrivalTime,
                departureTime = type.inbound?.arrivalTime,
                departureDate = type.inbound?.arrivalDate,
                origin = type.inbound?.origin,
                destination = type.inbound?.destination,
                elapsedTime = ""
            )
        )
}
