package com.rookia.gotflights.domain.model.mapper

import com.rookia.gotflights.domain.network.Flight


//todo convertir aquí a flight distinto, metiendo el tiempo total en días, horas...
object ApiToCacheMapper : BaseMapper<Flight, Flight> {
    override fun map(type: Flight): Flight =
        type
}

object CacheToFlight : BaseMapper<Flight, Flight> {
    override fun map(type: Flight): Flight =
        type
}