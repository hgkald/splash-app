package no.uio.ifi.in2000.team22.badeapp.data.enTur

class EnTurRepository {
    val data = EnTurDataSource()

    suspend fun getStops (lat: Double, lon: Double, radius: Int, size: Int): List<StopPlace> {
        return data.getStops(lat,lon,radius,size)
    }

}