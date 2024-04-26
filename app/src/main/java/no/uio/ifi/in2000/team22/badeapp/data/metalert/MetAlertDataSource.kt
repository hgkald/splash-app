package no.uio.ifi.in2000.team22.badeapp.data.metalert

import android.util.Log
import com.google.gson.annotations.SerializedName
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
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.alerts.RiskMatrixColor
import java.time.ZonedDateTime

private data class MetAlert(
    val features: List<Feature>,
    val lang: String,
    val lastChange: String
) {
    data class Feature(
        val properties: Properties,
        val geometry: Geometry,
        @SerializedName("when") val timeFrame: TimeFrame,
    ) {
        data class Properties(
            val area: String,
            val awarenessResponse: String,
            val awarenessLevel: String,
            val certainty: String,
            val consequences: String,
            val description: String,
            val event: String,
            val eventAwarenessName: String,
            val eventEndingTime: String,
            val instruction: String,
            val severity: String,
            val title: String,
            val riskMatrixColor: RiskMatrixColor
        )

        data class Geometry(
            val type: String,
            val coordinates: Any
        )

        data class TimeFrame(
            val interval: List<String>
        )
    }
}

class MetAlertDataSource {
    //private val path = "weatherapi/metalerts/2.0/test.json" // Test Path
    //private val path = "weatherapi/metalerts/2.0/current.json"

    private val endpoint = MetAPI.MetAlerts.TEST_ENDPOINT

    private val client =
        HttpClient {
            defaultRequest {
                header(MetAPI.API_KEY_NAME, MetAPI.API_KEY)
                url {
                    protocol = URLProtocol.HTTPS
                    host = MetAPI.HOST
                    path(MetAPI.MetAlerts.PATH)
                }
            }

            install(ContentNegotiation) {
                gson()
            }
        }

    private suspend fun fetchBody(): MetAlert {
        return try {
            val response = client.get(endpoint)
            Log.d("MetAlertDataSource", "Fetching new alerts ")
            response.body<MetAlert>()
        } catch (e: Exception) {
            Log.d("MetAlertDataSource", e.message.toString())
            Log.d("MetAlertDataSource", e.stackTrace.contentToString())
            MetAlert(
                features = emptyList(),
                lang = "",
                lastChange = ""
            )
        }
    }

    private fun toAlerts(metAlert: MetAlert): List<Alert> {
        val list: List<Alert> =
            metAlert.features.map {
                Alert(
                    geographicArea = if (it.geometry.type.lowercase() == "polygon") { // Type is Polygon
                        mutableListOf(it.geometry.coordinates as List<List<Double>>)
                    } else { // Type is MultiPolygon
                        it.geometry.coordinates as List<List<List<Double>>>
                    },
                    areaName = it.properties.area,
                    awarenessResponse = it.properties.awarenessResponse,
                    awarenessLevel = it.properties.awarenessLevel,
                    certainty = it.properties.certainty,
                    consequences = it.properties.consequences,
                    description = it.properties.description,
                    event = it.properties.event,
                    eventAwarenessName = it.properties.eventAwarenessName,
                    eventEndingTime = ZonedDateTime.parse(it.properties.eventEndingTime)
                        .toInstant(),
                    instruction = it.properties.instruction,
                    severity = it.properties.severity,
                    title = it.properties.title,
                    timeFrame = listOf(it.timeFrame.interval[0], it.timeFrame.interval[1]),
                    riskMatrixColor = it.properties.riskMatrixColor,
                    lastChange = metAlert.lastChange,
                )
            }
        return list
    }

    suspend fun fetchAlerts(): List<Alert> {
        return toAlerts(fetchBody())
    }

    suspend fun fetchAlertsForPosition(lat: Double, long: Double): List<Alert> {
        try {
            val response = client.get {
                url {
                    path(endpoint)
                    parameters.append("lat", lat.toString())
                    parameters.append("lon", long.toString())
                }
            }
            val body = response.body<MetAlert>()
            Log.d("MetAlertDataSource", "Fetching new alerts ")
            return toAlerts(body)
        } catch (e: Exception) {
            Log.d("MetAlertDataSource", e.message.toString())
            Log.d("MetAlertDataSource", e.stackTrace.contentToString())
            return emptyList()
        }
    }

    suspend fun fetchLastChanged(): String {
        return fetchBody().lastChange
    }
}


// Main function for testing the code along the way
suspend fun main() {
    val data = MetAlertDataSource()
    val info = data.fetchAlerts()

    info.forEach {
        println(it.areaName)
        println(it.description)
        println(it.riskMatrixColor)
        println()
        //println(it.properties)
    }
}