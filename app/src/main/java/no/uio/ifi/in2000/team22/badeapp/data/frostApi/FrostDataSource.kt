package no.uio.ifi.in2000.team22.badeapp.data.frostApi

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team22.badeapp.data.FrostAPI
import no.uio.ifi.in2000.team22.badeapp.model.forecast.WaterTemperature
import java.time.Instant

private data class Data(
    val tseries: List<Tseries>
)

private data class Tseries(
    val header: Header,
    val observations: List<Observation>
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

private data class Observation(
    val time: String,
    val body: ObservationTemperature
)

private data class ObservationTemperature(
    val value: String
)


private data class Pos(
    val lat: String,
    val lon: String
)


private data class FrostData(
    val data: Data
)

/**
 * A class used to fetch data from the Havvarsel-Frost API
 */
class FrostDataSource {
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
     * Fetches observations or metadata from FrostAPI and stores it in FrostData.
     * Which swimspots these observations are from are based on a point, a radius from this point,
     * and how many observations which should be included.
     *
     * @param [lat] latitude of point used as center
     * @param [lon] longitude of point used as center
     * @param maxDist only data inside this radius will be included
     * @param maxCount max amount of swimspots to include, result could include fewer
     * @param withObservations if true only observations will be included, if false only metadata
     *
     * @return An instance of [FrostData] which could include
     * either metadata or observations based on [withObservations]
     */
    private suspend fun fetch(
        lat: Double,
        lon: Double,
        maxDist: Double,
        maxCount: Double,
        withObservations: Boolean,
        from: Instant,
        to: Instant
    ): FrostData? {
        return try {
            Log.i("FrostDataSource", "Fetching data from API")
            val response = client.get {
                url {
                    parameters.append("incobs", withObservations.toString())
                    parameters.append(
                        "nearest",
                        """{"maxdist":$maxDist,"maxcount":$maxCount,"points":[{"lon":$lon,"lat":$lat}]}"""
                    )
                    parameters.append("time", "$from/$to")
                }
            }

            if (response.status == HttpStatusCode.NotFound) {
                Log.e(
                    "FrostDataSource",
                    "404 Not Found on request parameters = ${response.request.url.parameters}"
                )
                return null
            }

            response.body<FrostData>()
        } catch (e: Exception) {
            Log.e("FrostDataSource", e.message.toString())
            Log.e("FrostDataSource", e.stackTrace.toString())
            null
        }
    }

    /**
     * Uses [FrostDataSource] to fetch all temperature observations of every swimspot inside a radius
     * of 2km. Parses the timestaps to [Instant], so that it can be used later.
     *
     * @param [lat] latitude of swimspot
     * @param [lon] longitude of swimspot
     *
     * @return A list of [WaterTemperature] which contains temperature and time. Or if no observations
     * where found, an [emptyList] is returned.
     */
    suspend fun fetchWaterTemperature(
        lat: Double,
        lon: Double,
        from: Instant,
        to: Instant
    ): List<WaterTemperature> {
        val data =
            fetch(
                lat = lat,
                lon = lon,
                maxDist = 2.0,
                maxCount = 5.0,
                withObservations = true,
                from = from,
                to = to
            )

        return try {
            data?.data?.tseries?.map {
                val observation = it.observations.first()
                WaterTemperature(
                    temperature = observation.body.value.toDouble(),
                    time = Instant.parse(observation.time)
                )
            }.orEmpty()
        } catch (e: Exception) {
            Log.e("FrostDataSource", e.message.toString())
            Log.e("FrostDataSource", e.stackTrace.toString())
            return emptyList()
        }
    }
}
