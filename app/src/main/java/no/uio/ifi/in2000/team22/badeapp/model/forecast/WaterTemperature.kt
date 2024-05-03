package no.uio.ifi.in2000.team22.badeapp.model.forecast

import java.time.Instant

data class WaterTemperature(
    val temperature: Double,
    val time: Instant
)