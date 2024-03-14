package no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi

import no.uio.ifi.in2000.team22.badeapp.model.forecast.CurrentWeather

class LocationforecastRepository (
    private val locationforecastDataSource: LocationforecastDataSource
) {
    suspend fun fetchCurrentWeather(lat: Double, lon:Double) : CurrentWeather {
        return locationforecastDataSource.fetchCurrentWeather(lat, lon)
    }
}