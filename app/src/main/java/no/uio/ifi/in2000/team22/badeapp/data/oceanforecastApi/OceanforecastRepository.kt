package no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi

import no.uio.ifi.in2000.team22.badeapp.model.forecast.OceanForecast


/**
 * Class for external use of OceanForecast API
 */
class OceanforecastRepository(
    private val oceanForecastDataSource: OceanforecastDataSource
) {
    /**
     * function to fetch temperature from [OceanforecastDataSource]
     * @return [OceanForecast] object
     * @property [lat] latitude, [lon] - longitude
     */
    suspend fun fetchTemperature(lat: Double, lon: Double): OceanForecast? {
        return oceanForecastDataSource.fetchTemperature(lat, lon)
    }

}