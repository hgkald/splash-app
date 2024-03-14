package no.uio.ifi.in2000.team22.badeapp.data.metalert

import com.google.gson.annotations.SerializedName
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson

private data class MetAlert(
    val features : List<Feature>,
    val lang : String,
    val lastChange : String
)

private class Feature(
    val properties: Properties,
    val geometry: Geometry,
    @SerializedName("when") val timeFrame : TimeFrame,
)

private data class Geometry(
    val type : String,
    val coordinates : Any
)

enum class RiskMatrixColor {
    Yellow, Orange, Red
}

private data class Properties(
    val area : String,
    val awarenessResponse : String,
    val awarenessLevel : String,
    val certainty : String,
    val consequences : String,
    val description : String,
    val eventAwarenessName : String,
    val instruction : String,
    val severity : String,
    val title : String,
    val riskMatrixColor : RiskMatrixColor
)

private data class TimeFrame(
    val interval : List<String>
)

data class Alert(
    val geograficArea : List<List<List<Double>>>,
    val areaName : String,
    val awarenessResponse : String,
    val awarenessLevel : String?,
    val certainty : String,
    val consequences : String,
    val description : String,
    val eventAwarenessName : String,
    val instruction : String,
    val severity : String,
    val title : String,
    val timeFrame: List<String>,
    val riskMatrixColor: RiskMatrixColor,
    val lastChange: String,
)


class MetAlertDataSource {
    //private val path = "weatherapi/metalerts/2.0/test.json" // Test Path
    private val path = "weatherapi/metalerts/2.0/current.json"

    private val client =
        HttpClient {
            defaultRequest {
                url("https://gw-uio.intark.uh-it.no/in2000/")
                header("X-Gravitee-API-Key", "1257d958-d771-4767-abb0-9d78c0f45025")
            }

            install(ContentNegotiation) {
                gson()
            }
        }

    private suspend fun fetchBody(): MetAlert {
        val response = client.get(path)
        return response.body<MetAlert>()
    }

    private fun toAlerts(metAlert: MetAlert) : List<Alert>{
        var list : List<Alert> = emptyList()
        metAlert.features.forEach{
            list += listOf(Alert(
                geograficArea = if (it.geometry.type.lowercase() == "polygon"){ // Type is Polygon
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
                eventAwarenessName = it.properties.eventAwarenessName,
                instruction = it.properties.instruction,
                severity = it.properties.severity,
                title = it.properties.title,
                timeFrame = listOf(it.timeFrame.interval[0], it.timeFrame.interval[1]),
                riskMatrixColor = it.properties.riskMatrixColor,
                lastChange = metAlert.lastChange,
            ))
        }
        return list
    }

    suspend fun fetchAlerts() : List<Alert>{
        return toAlerts(fetchBody())
    }


    suspend fun fetchAlertsForPosition(lat : Double, long : Double) : List<Alert> {
        val response = client.get(path + "?lat=${lat}&lon=${long}")
        val body = response.body<MetAlert>()
        return toAlerts(body)
    }

    suspend fun fetchLastChanged() : String{
        return fetchBody().lastChange
    }
}




// Main function for testing the code along the way
suspend fun main(){
    val data = MetAlertDataSource()
    val info = data.fetchAlerts()

    info.forEach{
        println(it.areaName)
        println(it.description)
        println(it.riskMatrixColor)
        println()
        //println(it.properties)
    }
}