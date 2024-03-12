package no.uio.ifi.in2000.team22.badeapp.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson

class Datasource {
    private val path : String = ""
    private val client = HttpClient{
        defaultRequest {
            url("https://api.entur.io/geocoder/v1/reverse")
        }
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun getdData(lat: Double, lon: Double, radius : Int, size: Int, layers : String){
        val response = client.get {
            url{
                parameters.append("point.lat", "$lat,")
                parameters.append("point.lon", "$lon,")
                parameters.append("boundary_circle_radius", "$radius,")
                parameters.append("size", "$size,")
                parameters.append("layers", "$layers,")
            }
        }
    }
}