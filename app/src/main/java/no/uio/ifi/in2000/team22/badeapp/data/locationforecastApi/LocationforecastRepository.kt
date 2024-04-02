package no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi

import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather

class LocationforecastRepository (
    private val locationforecastDataSource: LocationforecastDataSource
) {
    suspend fun fetchCurrentWeather(lat: Double, lon: Double) : Weather {
        //Returns weather forecast for the current hour
        return locationforecastDataSource.fetchWeather(lat, lon)
    }

    suspend fun fetchWeatherForecast(lat: Double, lon: Double, hourOffset: Int): Weather {
        //Returns weather forecast hours in the future, as given by hourOffset
        return locationforecastDataSource.fetchWeather(lat, lon, hourOffset)
    }
}