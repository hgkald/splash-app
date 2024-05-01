package no.uio.ifi.in2000.team22.badeapp.data.frostApi

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team22.badeapp.data.FrostAPI
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

private data class Data(
    val tseries: List<Tseries>
)

private data class Tseries(
    val header: Header
)

private data class Header(
    val id: Id,
    val extra: Extra
)

private data class Id(
    val buoyid: String,
    val parameter: String,
    val source: String
)

private data class Extra(
    val name: String,
    val pos: Pos
)


private data class Pos(
    val lat: String,
    val lon: String
)


private data class FrostData(
    val data: Data
)

class FrostDataSource {
    private val path = ""
    private val client =
        HttpClient {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = FrostAPI.HOST
                    path(FrostAPI.SWIMSPOTS_PATH)
                }
            }

            install(ContentNegotiation) {
                gson()
            }
        }

    /**
     * Retrieves all swimspots from the FrostAPI.
     *
     * @return a list of the class SwimSpot
     */
    suspend fun getAllSwimSpots(): List<Swimspot> {
        val response: FrostData = try {
            client.get("").body<FrostData>()
        } catch (e: Exception) {
            Log.e("FrostDataSource", "failed to get data from Frost: ${e.message}")
            return emptyList<Swimspot>()
        }

        val swimspots: List<Swimspot> = try {
            response.data.tseries
                .filter { it -> it.header.extra.pos.lat != "None" || it.header.extra.pos.lat != "None" }
                .map { it ->
                    Swimspot(
                        name = it.header.extra.name,
                        lat = it.header.extra.pos.lat.toDouble(),
                        lon = it.header.extra.pos.lon.toDouble()
                    )
                }
        } catch (e: Exception) {
            Log.e("FrostDataSource", "failed to parse data to SwimSpots: ${e.message}")
            emptyList<Swimspot>()
        }

        return swimspots
    }

    private suspend fun getData(
        maxDist: Double,
        maxCount: Int,
        lon: Double,
        lat: Double
    ): FrostData {
        val response = client.get(path) {
            url {
                parameters.append(
                    "nearest",
                    """{"maxdist":$maxDist,"maxcount":$maxCount,"points":[{"lon":$lon,"lat":$lat}]}"""
                )

            }
        }
        Log.i("FrostDataSource", "Sending request to frost.")
        val body = response.body<FrostData>()
        return body
    }

    suspend fun getNearbyFromCoords(
        maxDist: Double,
        maxCount: Int,
        lon: Double,
        lat: Double
    ): List<Swimspot> {
        val data = FrostDataSource()
        val verdi = data.getData(maxDist, maxCount, lon, lat)
        val badesteder = mutableListOf<Swimspot>()

        for (badestedene in verdi.data.tseries) {
            val name = badestedene.header.extra.name
            val lon = badestedene.header.extra.pos.lon.toDouble()
            val lat = badestedene.header.extra.pos.lat.toDouble()
            val badested = Swimspot(name = name, lon = lon, lat = lat)
            badesteder.add(badested)

        }
        return badesteder
    }
}