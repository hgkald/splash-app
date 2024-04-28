package no.uio.ifi.in2000.team22.badeapp.data.enTur

import no.uio.ifi.in2000.team22.badeapp.model.transport.TransportCategory

class EnTurRepository {
    val data: EnTurDataSource = EnTurDataSource()

    suspend fun getStops(lat: Double, lon: Double, radius: Int, size: Int): List<StopPlace> {
        return data.getStops(lat, lon, radius, size)
    }

    suspend fun getAvailableTransportCategories(
        lat: Double,
        lon: Double,
        radius: Int,
        size: Int
    ): List<TransportCategory> {
        val stops = data.getStops(lat = lat, lon = lon, radius = radius, size = size)

        return stops
            .asSequence()
            .map { stop -> stop.category.map { TransportCategory.fromString(it) } }
            .flatten()
            .filterNotNull()
            .toSet()
            .toList()
    }
}