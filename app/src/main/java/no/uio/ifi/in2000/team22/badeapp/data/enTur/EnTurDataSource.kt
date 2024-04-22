package no.uio.ifi.in2000.team22.badeapp.data.enTur

import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
    }

    suspend fun getData(lat: Double, lon: Double, radius: Int, size: Int, layers: String): Root {
        val response = client.get(path) {
            url {
                parameters.append("point.lat", "$lat")
                parameters.append("point.lon", "$lon")
                parameters.append("boundary.circle.radius", "$radius")
                parameters.append("size", "$size")
                parameters.append("layers", layers)
            }
        }
        //Log.d("EnTurDataSource", "HTTP status : ${response.status}")
        return response.body<Root>()
    }

    suspend fun getStops(lat: Double, lon: Double, radius: Int, size: Int): List<StopPlace> {

        val stops = mutableListOf<StopPlace>()
        val data = getData(lat, lon, radius, size, layers = "venue")

        data.features.forEach {
            val properties = it.properties
            val coordinates = it.geometry.coordinates
            val name = properties.name
            val type = properties.category
            val label = properties.label

            stops.add(StopPlace(coordinates[1], coordinates[0], name, type, label))
            //Log.d("EnTurDataSource", "Stop : $type + $name")

        }
        return stops
    }
}