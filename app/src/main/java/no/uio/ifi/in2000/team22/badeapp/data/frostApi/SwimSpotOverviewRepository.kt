package no.uio.ifi.in2000.team22.badeapp.data.frostApi

import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

object SwimSpotOverviewRepository {
    private val repo = FrostRepository()
    val swimSpots: List<Swimspot> = runBlocking {
        var id: Int = 0
        repo.getAllSwimspots().map {
            Swimspot(id = id++, lat = it.lat, lon = it.lon, name = it.name)
        }
    }

}