package no.uio.ifi.in2000.team22.badeapp.data.frostApi

class FrostRepository{
    val frostDataSource = FrostDataSource()
    var badesteder: List<Badested> = listOf()
    suspend fun getBadeplasser(maxDist : Double, maxCount : Int, lon : Double, lat : Double) : List<Badested>{
        badesteder = (frostDataSource.getNearbyFromCoords(maxDist, maxCount, lon, lat))
    return badesteder
    }


}