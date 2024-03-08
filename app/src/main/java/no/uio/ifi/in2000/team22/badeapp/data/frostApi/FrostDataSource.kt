package no.uio.ifi.in2000.team22.badeapp.data.frostApi

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson
import kotlin.math.max

class FrostDataSource {

    //TESTDATA
//    val maxDist : Double = 10.0
//    val maxCount : Int = 3
//    val lon : Double = 5.314803
//    val lat : Double = 60.396388

    private val path = ""
    private val client =
        HttpClient {
            defaultRequest {
                url("https://havvarsel-frost.met.no/api/v1/obs/badevann/get")
//

            }

            install(ContentNegotiation) {
                gson()
            }
        }

    suspend fun getData(maxDist : Double, maxCount : Int, lon : Double, lat : Double): FrostData{
        val response = client.get(path) {
            url{
                parameters.append("nearest","""{"maxdist":$maxDist,"maxcount":$maxCount,"points":[{"lon":$lon,"lat":$lat}]}""")

            }
        }
        Log.i("FrostDataSource", "Sending request to frost.")
        val body = response.body<FrostData>()
        return body
    }
    suspend fun getNearbyFromCoords(maxDist : Double, maxCount : Int, lon : Double, lat : Double): List<Badested>{
        val data = FrostDataSource()
        val verdi = data.getData(maxDist, maxCount, lon, lat)
        val badesteder = mutableListOf<Badested>()

        for (badestedene in verdi.data.tseries) {
            val name = badestedene.header.extra.name
            val lon = badestedene.header.extra.pos.lon.toDouble()
            val lat = badestedene.header.extra.pos.lat.toDouble()
            val badested = Badested(name, lon, lat)
            badesteder.add(badested)

        }
        return badesteder
//              TEST INFO
//        for (badested in verdi.data.tseries) {
//            println("Name: ${badested.header.extra.name}")
//            println("Lon: ${badested.header.extra.pos.lon}")
//            println("Lat: ${badested.header.extra.pos.lat}")
//        }
//    println(verdi)
    }
}


// TEST FUNCTION
//suspend fun main(){
//    val frostDataSource = FrostDataSource()
//    println(frostDataSource.getNearbyFromCoords(frostDataSource.maxDist, frostDataSource.maxCount, frostDataSource.lon, frostDataSource.lat))
//}