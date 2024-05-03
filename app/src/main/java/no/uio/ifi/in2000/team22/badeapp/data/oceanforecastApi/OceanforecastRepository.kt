package no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi

import android.os.Build
import androidx.annotation.RequiresApi
import no.uio.ifi.in2000.team22.badeapp.model.forecast.OceanForecast
import no.uio.ifi.in2000.team22.badeapp.model.forecast.WaterTemperature
import java.time.Instant


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
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchTemperature(lat: Double, lon: Double): WaterTemperature? {
        val temp = oceanForecastDataSource.fetchTemperature(lat, lon) ?: return null

        return WaterTemperature(
            temperature = temp.waterTemperature,
            time = Instant.parse(temp.time)
        )
    }

}