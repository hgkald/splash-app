package no.uio.ifi.in2000.team22.badeapp.data.frostApi

import android.util.Log
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot

object SwimSpotOverviewRepository {
    private val repo = FrostRepository()
    val swimSpots: List<SwimSpot> = runBlocking {
        var id: Int = 0
        repo.getAllSwimspots().map {
            Log.i("SwimSpotOverRepo", "Current id: $id")
            SwimSpot(id = id++, lat = it.lat, lon = it.lon, name = it.name)
        }
    }

}