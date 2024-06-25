package no.uio.ifi.in2000.team22.badeapp.data.enTur

import no.uio.ifi.in2000.team22.badeapp.model.transport.TransportCategory

class EnTurRepository {
    val data: EnTurDataSource = EnTurDataSource()

    /**
     * Fetches a list a public transport stops using [EnTurDataSource]
     *
     * @param lat latitude position
     * @param lon longitude position
     * @param radius radius around the given point to fetch stops
     * @param size is the maximum number of stops to return
     *
     * @return A list of [StopPlace]
     */
    suspend fun getStops(lat: Double, lon: Double, radius: Int, size: Int): List<StopPlace> {
        return data.getStops(lat, lon, radius, size)
    }

    /**
     * Returns a list of all the available transport categories. It combines a list of
     * [StopPlace] to a smaller list of [TransportCategory].
     *
     * @param lat latitude position
     * @param lon longitude position
     * @param radius radius around the given point to fetch categories
     * @param size is the maximum number of categories
     *
     * @return A list of [StopPlace]
     */
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