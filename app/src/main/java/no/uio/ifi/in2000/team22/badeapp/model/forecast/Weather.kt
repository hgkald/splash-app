package no.uio.ifi.in2000.team22.badeapp.model.forecast

import java.time.Instant

data class Weather(
    val time: Instant?,
    val airTemperature: Double?,
    val symbolCode: String?,
    val windSpeed: Double?,
    val windFromDirection: Double?,
    val uvIndex: Double?,
    val precipitationNextHour: Double?,
)

val nullWeather =
    Weather(
        time = null,
        airTemperature = null,
        symbolCode = null,
        windSpeed = null,
        windFromDirection = null,
        uvIndex = null,
        precipitationNextHour = null
)
