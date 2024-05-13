package no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team22.badeapp.data.MetAPI
import no.uio.ifi.in2000.team22.badeapp.data.round
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Locationforecast
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather

class LocationforecastDataSource {
    private val client =
        HttpClient {
            defaultRequest {
                header(MetAPI.API_KEY_NAME, MetAPI.API_KEY)
                url {
                    protocol = URLProtocol.HTTPS
                    host = MetAPI.HOST
                    path(MetAPI.LocationForecast.PATH, MetAPI.LocationForecast.TYPE)
                }
            }

            install(ContentNegotiation) {
                gson()
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }

            install(HttpCache)
        }

    private suspend fun fetch(lat: Double, lon: Double): Locationforecast? {
        return try {
            val response = client.get {
                url {
                    parameters.append("lat", lat.round(3).toString())
                    parameters.append("lon", lon.round(3).toString())
                }
            }
            Log.e("LocationforecastDataSource", "Fetching new location forecast")
            response.body<Locationforecast>()
        } catch (e: Exception) {
            Log.e("LocationforecastDataSource", e.message.toString())
            Log.e("LocationforecastDataSource", e.stackTrace.toString())
            null
        }
    }

    suspend fun fetchWeather(lat: Double, lon: Double, hourOffset: Int = 0): Weather {
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
