package no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi
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
import no.uio.ifi.in2000.team22.badeapp.model.forecast.CurrentWeather
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Locationforecast

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
        }

    private suspend fun fetch(lat: Double, lon: Double): Locationforecast? {
        return try {
            val response = client.get {
                url {
                    parameters.append("lat", lat.toString())
                    parameters.append("lon",lon.toString())
                }
            }
            response.body<Locationforecast>()
        } catch (e: Exception) {
            Log.d("LocationforecastDataSource", e.message.toString())
            Log.d("LocationforecastDataSource", e.stackTrace.toString())
            null
        }
    }

    suspend fun fetchCurrentWeather(lat: Double, lon: Double) : CurrentWeather {
        try {
            val forecast = fetch(lat, lon)
            val firstTimeseries = forecast?.properties?.timeseries?.get(0)
            return CurrentWeather(
                time = firstTimeseries?.time,
                airTemperature = firstTimeseries?.data?.instant?.details?.air_temperature,
                symbolCode = firstTimeseries?.data?.next_1_hours?.summary?.symbol_code,
                windSpeed = firstTimeseries?.data?.instant?.details?.wind_speed,
                windFromDirection = firstTimeseries?.data?.instant?.details?.wind_from_direction,
                uvIndex = firstTimeseries?.data?.instant?.details?.ultraviolet_index_clear_sky,
                precipitationNextHour = firstTimeseries?.data?.next_1_hours?.details?.precipitation_amount
            )
        }
        catch (e: Exception) {
            Log.d("LocationforecastDataSource", e.message.toString())
            Log.d("LocationforecastDataSource", e.stackTrace.toString())
            return CurrentWeather(
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
