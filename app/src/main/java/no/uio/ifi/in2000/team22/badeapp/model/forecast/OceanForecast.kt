package no.uio.ifi.in2000.team22.badeapp.model.forecast

/**
 * Data class for OceanForecast objects to get current water temperature
 */
data class OceanForecast(
    val time: String,
    val waterTemperature: Double
)