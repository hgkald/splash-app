package no.uio.ifi.in2000.team22.badeapp.data.frostApi

@Deprecated(
    "This code was used to make working with Met's FrostAPI easier.",
    ReplaceWith("SwimspotDataSource"),
    DeprecationLevel.WARNING
)
object SwimSpotOverviewRepository {
    private val repo = FrostRepository()

//    val swimSpots: List<Swimspot> = runBlocking {
//        var id: Int = 0
//        repo.getAllSwimspots().map {
//            Swimspot(id = id++, lat = it.lat, lon = it.lon, name = it.name)
//        }
//    }

}