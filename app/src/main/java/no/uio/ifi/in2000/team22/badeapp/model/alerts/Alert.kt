package no.uio.ifi.in2000.team22.badeapp.model.alerts

import java.time.Instant

enum class RiskMatrixColor(val norsk: String) {
    Green("Grønt"),
    Yellow("Gult"),
    Orange ("Oransje"),
    Red("Rødt")
}

data class Alert(
    val geographicArea : List<List<List<Double>>>,
    val areaName : String,
    val awarenessResponse : String,
    val awarenessLevel : String?,
    val certainty : String,
    val consequences : String,
    val description : String,
    val event: String,
    val eventAwarenessName : String,
    val eventEndingTime: Instant,
    val instruction : String,
    val severity : String,
    val title : String,
    val timeFrame: List<String>,
    val riskMatrixColor: RiskMatrixColor,
    val lastChange: String,
)


