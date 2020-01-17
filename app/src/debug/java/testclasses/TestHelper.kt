package testclasses

import com.rookia.gotflights.domain.network.Flight
import com.rookia.gotflights.domain.network.Inbound
import com.rookia.gotflights.domain.network.Outbound
import java.math.BigDecimal


fun getFlight(origin: String, destination: String, price: BigDecimal): Flight =
    Flight(
        currency = "EUR",
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
        price = price
    )