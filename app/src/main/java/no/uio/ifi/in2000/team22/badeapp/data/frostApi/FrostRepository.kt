package no.uio.ifi.in2000.team22.badeapp.data.frostApi

import no.uio.ifi.in2000.team22.badeapp.model.forecast.WaterTemperature

class FrostRepository {
    private val frostDataSource = FrostDataSource()

    /**
     * Uses [FrostDataSource] to fetch the latest water temperature for swimspots inside a 2km radius
     *
     * @param [lat] latitude of swimspot
     * @param [lon] longitude of swimspot
     *
     * @return An instance of [WaterTemperature] which contains temperature and time
     */
    suspend fun fetchWaterTemperature(lat: Double, lon: Double): WaterTemperature? {
        return try {
            frostDataSource.fetchWaterTemperature(lat = lat, lon = lon).first()
        } catch (e: NoSuchElementException) {
            null
        }
    }
}