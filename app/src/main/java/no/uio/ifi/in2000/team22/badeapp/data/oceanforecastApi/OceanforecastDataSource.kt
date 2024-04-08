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
import no.uio.ifi.in2000.team22.badeapp.data.MetAPI
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather


data class OceanForecast(
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

    private suspend fun fetch(lat: Double, lon: Double): OceanForecast? {
        return try {
            val response = client.get {
                url {
                    parameters.append("lat", lat.toString())
                    parameters.append("lon", lon.toString())
                }
            }
            response.body<OceanForecast>()
        } catch (e: Exception) {
            Log.e("OceanForecastDataSource", e.message.toString())
            Log.e("OceanForecastDataSource", e.stackTrace.toString())
            null
        }
    }


    suspend fun fetchTemperature(lat: Double, lon: Double): Weather {
        //Returns a Weather object based in the current time
        // lat and lon: latitude and longitude
        // hourOffset: Weather this many hours into the future.
        if (hourOffset > 54) {
            Log.w(
                "LocationforecastDataSource",
                "fetchWeather() might return null Weather parameters (hourOffset > 54)"
            )
        }

        try {
            val forecast = fetch(lat, lon)
            val timeseries = forecast?.properties?.timeseries?.get(hourOffset)
            return Weather(
                time = timeseries?.time,
                airTemperature = timeseries?.data?.instant?.details?.air_temperature,
                symbolCode = timeseries?.data?.next_1_hours?.summary?.symbol_code,
                windSpeed = timeseries?.data?.instant?.details?.wind_speed,
                windFromDirection = timeseries?.data?.instant?.details?.wind_from_direction,
                uvIndex = timeseries?.data?.instant?.details?.ultraviolet_index_clear_sky,
                precipitationNextHour = timeseries?.data?.next_1_hours?.details?.precipitation_amount
            )
        } catch (e: Exception) {
            Log.e("LocationforecastDataSource", e.message.toString())
            Log.e("LocationforecastDataSource", e.stackTrace.toString())
            return Weather(
                time = null,
                airTemperature = null,
                symbolCode = null,
                windSpeed = null,
                windFromDirection = null,
                uvIndex = null,
                precipitationNextHour = null
            )
        }
    }

}