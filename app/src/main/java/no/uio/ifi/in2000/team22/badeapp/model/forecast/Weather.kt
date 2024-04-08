package no.uio.ifi.in2000.team22.badeapp.model.forecast

data class Weather (
    val time: String?,
    val airTemperature : Double?,
    val symbolCode: String?,
    val windSpeed: Double?,
    val windFromDirection: Double?,
    val uvIndex: Double?,
    val precipitationNextHour: Double?,
)
