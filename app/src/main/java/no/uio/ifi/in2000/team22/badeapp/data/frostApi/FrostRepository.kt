package no.uio.ifi.in2000.team22.badeapp.data.frostApi

class FrostRepository{
    val frostDataSource = FrostDataSource()
    suspend fun getBadeplasser(maxDist : Double, maxCount : Int, lon : Double, lat : Double) : List<Badested>{
        return (frostDataSource.getNearbyFromCoords(maxDist,maxCount, lon, lat))
    }


}