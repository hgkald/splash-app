package no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi

import no.uio.ifi.in2000.team22.badeapp.model.forecast.OceanForecast

class OceanforecastRepository(
    private val oceanForecastDataSource: OceanforecastDataSource
) {
    suspend fun fetchCurrentTemperature(lat: Double, lon: Double): OceanForecast? {
        return oceanForecastDataSource.fetchTemperature(lat, lon)
    }

}