package no.uio.ifi.in2000.team22.badeapp.data.frostApi

import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot

class FrostRepository {
    val frostDataSource = FrostDataSource()
    suspend fun getBadeplasser(
        maxDist: Double,
        maxCount: Int,
        lon: Double,
        lat: Double
    ): List<SwimSpot> {
        return (frostDataSource.getNearbyFromCoords(maxDist, maxCount, lon, lat))
    }


}