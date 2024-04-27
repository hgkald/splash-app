package no.uio.ifi.in2000.team22.badeapp.data.frostApi

import android.util.Log
import no.uio.ifi.in2000.team22.badeapp.model.forecast.WaterTemperature
import java.time.Instant
import java.time.temporal.ChronoUnit

class FrostRepository {
    private val frostDataSource = FrostDataSource()

    /**
     * Uses [FrostDataSource] to fetch the latest water temperature between [from] and [to],
     * for swimspots inside a 2km radius
     *
     * @param [lat] latitude of swimspot
     * @param [lon] longitude of swimspot
     * @param [from] start timestamp
     * @param [to] end timestamp
     *
     * @return An instance of [WaterTemperature] which contains temperature and time
     */
    suspend fun fetchWaterTemperatureWithTime(
        lat: Double,
        lon: Double,
        from: Instant,
        to: Instant
    ): WaterTemperature? {
        return try {
            val result = frostDataSource.fetchWaterTemperature(
                lat = lat,
                lon = lon,
                from = from,
                to = to
            ).first()
            Log.d(
                "FrostRepo",
                "Got water temperature. Temp = ${result.temperature}, Time=${result.time}"
            )

            //Returns result
            result
        } catch (e: NoSuchElementException) {
            null
        }
    }

    /**
     * Uses [FrostDataSource] to fetch the latest water temperature the last 2 months,
     * for swimspots inside a 2km radius
     *
     * @param [lat] latitude of swimspot
     * @param [lon] longitude of swimspot
     *
     * @return An instance of [WaterTemperature] which contains temperature and time
     */
    suspend fun fetchWaterTemperature(
        lat: Double,
        lon: Double,
    ): WaterTemperature? {
        return try {
            val toDate = Instant.now();
            val fromDate = toDate.minus(60, ChronoUnit.DAYS)

            fetchWaterTemperatureWithTime(
                lat = lat,
                lon = lon,
                from = fromDate,
                to = toDate
            )

        } catch (e: Exception) {
            null
        }
    }
}