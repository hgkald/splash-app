package no.uio.ifi.in2000.team22.badeapp.data.enTur

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson

class EnTurDataSource {
    private val path: String = ""

    private val client = HttpClient {
        defaultRequest {
            url("https://api.entur.io/geocoder/v1/reverse?")
        }
        install(ContentNegotiation) {
            gson()
        }
        install(HttpCache)
    }

    /**
     * Uses EnTur API to fetch an overview of POIs(points of interests) inside a certain radius
     * from a point.
     *
     * @param lat latitude position
     * @param lon longitude position
     * @param radius radius around the given point to fetch POIs
     * @param size is the maximum number of results (POIs)
     * @param layers what types of POIs to be fetched
     *
     * @return An instance of [Root] that include the requested information
     */

    suspend fun getData(lat: Double, lon: Double, radius: Int, size: Int, layers: String): Root? {
        return try {
            val response = client.get(path) {
                url {
                    parameters.append("point.lat", "$lat")
                    parameters.append("point.lon", "$lon")
                    parameters.append("boundary.circle.radius", "$radius")
                    parameters.append("size", "$size")
                    parameters.append("layers", layers)
                }
            }
            Log.d("EnTurDataSource", "HTTP status : ${response.status}")
            response.body<Root>()
        } catch (e: Exception) {
            Log.e("EnturDataSource", "Error while fetching data.")
            Log.e("EnturDataSource", e.message.toString())
            null
        }
    }

    /**
     * Fetches a list a public transport stops from a given point and inside a given radius.
     *
     * @param lat latitude position
     * @param lon longitude position
     * @param radius radius around the given point to fetch stops
     * @param size is the maximum number of stops to return
     *
     * @return A list of [StopPlace]
     */

    suspend fun getStops(lat: Double, lon: Double, radius: Int, size: Int): List<StopPlace> {

        val stops = mutableListOf<StopPlace>()
        val data = getData(lat, lon, radius, size, layers = "venue") ?: return emptyList()

        data.features.forEach {
            val properties = it.properties
            val coordinates = it.geometry.coordinates
            val name = properties.name
            val type = properties.category
            val label = properties.label

            stops.add(StopPlace(coordinates[1], coordinates[0], name, type, label))
            Log.d("EnTurDataSource", "Stop : $type + $name")

        }
        return stops
    }
}