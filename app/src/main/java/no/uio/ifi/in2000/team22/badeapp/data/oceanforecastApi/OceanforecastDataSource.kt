package no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.team22.badeapp.data.MetAPI
import no.uio.ifi.in2000.team22.badeapp.model.forecast.OceanForecast

/**
 * Data classes for OceanForecast API
 */

data class OceanForecastAPI(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

data class Properties(
    val meta: Meta,
    val timeseries: List<Timeseries>
)

data class Meta(
    val updated_at: String,
    val units: Units
)

data class Units(
    val sea_surface_wave_from_direction: String,
    val sea_surface_wave_height: String,
    val sea_water_speed: String,
    val sea_water_temperature: String,
    val sea_water_to_direction: String
)

data class Timeseries(
    val time: String,
    val data: Data
)

data class Data(
    val instant: Instant
)

data class Instant(
    val details: Details
)

data class Details(
    val sea_surface_wave_from_direction: Double,
    val sea_surface_wave_height: Double,
    val sea_water_speed: Double,
    val sea_water_temperature: Double,
    val sea_water_to_direction: Double
)

/**
 * Class for connecting and authentication for API
 */

class OceanforecastDataSource {
    private val client =
        HttpClient {
            defaultRequest {
                header(MetAPI.API_KEY_NAME, MetAPI.API_KEY)
                url {
                    protocol = URLProtocol.HTTPS
                    host = MetAPI.HOST
                    path(MetAPI.OceanForecast.PATH, MetAPI.OceanForecast.TYPE)
                }
            }

            install(ContentNegotiation) {
                gson()
            }
        }

    /**
     * Function to get API data.
     * @return [OceanForecastAPI]
     * @property [lat] latitude, [lon] - longitude
     */

    private suspend fun fetch(lat: Double, lon: Double): OceanForecastAPI? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = client.get {
                    url {
                        parameters.append("lat", lat.toString())
                        parameters.append("lon", lon.toString())
                    }
                }
                response.body<OceanForecastAPI>()
            } catch (e: Exception) {
                Log.e("OceanForecastDataSource", e.message.toString())
                Log.e("OceanForecastDataSource", e.stackTrace.toString())
                null
            }
        }
    }

    /**
     * Function to create an [OceanForecast] object from API data
     * @return [OceanForecast] object
     * @property [lat] latitude, [lon] - longitude
     */

    suspend fun fetchTemperature(lat: Double, lon: Double): OceanForecast? {


        try {
            val forecast = fetch(lat, lon)
            if (forecast?.properties?.timeseries?.get(2) == null) {
                return null
            }
            return OceanForecast(
                time = forecast.properties.timeseries.get(2).time,
                waterTemperature = forecast.properties.timeseries.get(2).data.instant.details.sea_water_temperature,
            )
        } catch (e: Exception) {
            Log.e("OceanforecastDataSource", e.message.toString())
            Log.e("OceanforecastDataSource", e.stackTrace.toString())
            return null

        }
    }

}
