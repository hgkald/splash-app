package no.uio.ifi.in2000.team22.badeapp.data.enTur

import android.util.Log

class EnTurRepository {
    val data : EnTurDataSource =  EnTurDataSource()

    suspend fun getStops (lat: Double, lon: Double, radius: Int, size: Int,): List<StopPlace> {
        return data.getStops(lat,lon,radius,size)
    }

}