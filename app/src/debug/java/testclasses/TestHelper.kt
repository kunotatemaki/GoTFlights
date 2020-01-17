package testclasses

import com.rookia.gotflights.domain.model.Flight
import com.rookia.gotflights.domain.model.Inbound
import com.rookia.gotflights.domain.model.Outbound
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
            destination = destination,
            elapsedTime = ""
        ),
        outbound = Outbound(
            airline = "airline",
            airlineImage = "image",
            arrivalDate = "20/02/1902",
            arrivalTime = "20:08",
            departureDate = "20/02/1902",
            departureTime = "20:08",
            origin = destination,
            destination = origin,
            elapsedTime = ""
        ),
        price = price
    )