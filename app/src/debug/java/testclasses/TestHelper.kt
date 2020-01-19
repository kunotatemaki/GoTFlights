package testclasses

import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.model.Inbound
import com.rookia.gotflights.domain.model.Outbound
import com.rookia.gotflights.domain.network.model.FlightApiResponse
import com.rookia.gotflights.domain.network.model.InboundApiResponse
import com.rookia.gotflights.domain.network.model.OutboundApiResponse


fun getFlight(origin: String, destination: String, price: Double, currencyName: String = "EUR"): Flight =
    Flight(
        currency = currencyName,
        inbound = Inbound(
            airline = "airline",
            airlineImage = "image",
            arrivalDate = "20/02/1902",
            arrivalTime = "20:08",
            departureDate = "20/02/1902",
            departureTime = "20:08",
            origin = origin,
            destination = destination
        ),
        outbound = Outbound(
            airline = "airline",
            airlineImage = "image",
            arrivalDate = "20/02/1902",
            arrivalTime = "20:08",
            departureDate = "20/02/1902",
            departureTime = "20:08",
            origin = destination,
            destination = origin
        ),
        price = price,
        targetCurrencyName = "EUR"
    )

fun getFlightFromNetwork(origin: String, destination: String, price: Double): FlightApiResponse =
    FlightApiResponse(
        currency = "EUR",
        inbound = InboundApiResponse(
            airline = "airline",
            airlineImage = "image",
            arrivalDate = "20/02/1902",
            arrivalTime = "20:08",
            departureDate = "20/02/1902",
            departureTime = "20:08",
            origin = origin,
            destination = destination
        ),
        outbound = OutboundApiResponse(
            airline = "airline",
            airlineImage = "image",
            arrivalDate = "20/02/1902",
            arrivalTime = "20:08",
            departureDate = "20/02/1902",
            departureTime = "20:08",
            origin = destination,
            destination = origin
        ),
        price = price
    )