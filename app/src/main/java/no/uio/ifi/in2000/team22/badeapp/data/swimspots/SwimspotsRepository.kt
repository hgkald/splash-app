package no.uio.ifi.in2000.team22.badeapp.data.swimspots

import android.content.Context
import android.location.Location
import android.util.Log
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

class SwimspotsRepository(private val context: Context) {
    private val datasource = SwimspotsDataSource
    private var swimspots = emptyList<Swimspot>()
    private var swimspotsDistanceFrom: Pair<Double, Double>? = null

    /**
     * Gets a list of all swimspots from [SwimspotsDataSource]. This runs disk IO, and should use Dispatchers.IO
     *
     * @return A list of [Swimspot]
     */
    suspend fun getAllSwimspots(): List<Swimspot> {
        if (swimspots.isNotEmpty()) {
            return swimspots
        }

        swimspots = datasource.getSwimspots(context)
        return swimspots
    }

    /**
     * Retrieves a single Swimspot-element from the datasource based on [id]
     *
     * @param id of [Swimspot.id]
     * @return [Swimspot] with matching [id]. Returns null if no such element is found.
     */
    suspend fun getSwimspotById(id: String): Swimspot? {
        return try {
            getAllSwimspots().first { it.id == id.toInt() }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Returns true if old location is more than 250m away from current location
     *  or if distances have not previously been calculated
     */
    private fun shouldRecalculateDistance(lat: Double, lon: Double): Boolean {
        val results: FloatArray = floatArrayOf(0F)
        return if (swimspotsDistanceFrom != null) {
            Location.distanceBetween(
                swimspotsDistanceFrom!!.first,
                swimspotsDistanceFrom!!.second,
                lat,
                lon,
                results
            )
            results[0] > 250
        } else {
            true
        }
    }

    /**
     * Calculates distances from latitude and longitude to each swimspot.
     *
     * @param latitude
     * @param longitude
     */
    private fun calculateDistancesFrom(
        latitude: Double,
        longitude: Double,
    ){
        try {
            swimspots
                .map {
                    val results: FloatArray = floatArrayOf(0F)
                    Location.distanceBetween(latitude, longitude, it.lat, it.lon, results)

                    it.distance = results.first()
                    it
                }
            swimspotsDistanceFrom = Pair(latitude, longitude)
        } catch (e: Exception) {
            Log.d("SwimspotsRepo", "Exception at getNearestSwimspots()")
            swimspotsDistanceFrom = null
        }
    }

    /**
     * Return the x nearest swimspots from a given point. Calculates distance if not already done
     *
     * @param latitude
     * @param longitude
     * @param limit the max amount if swimspots returned, default is size of [swimspots]
     */
    fun getNearestSwimspots(
        latitude: Double,
        longitude: Double,
        limit: Int = swimspots.size
    ): List<Swimspot> {
        if (shouldRecalculateDistance(latitude, longitude)) {
            calculateDistancesFrom(latitude, longitude)
            Log.d("SwimspotsRepository", "Distances (re)calculated for $latitude, $longitude")
        }
        val swimspotsSorted = try {
            swimspots
                .sortedBy {
                    it.distance
                }.subList(0, limit - 1)
        } catch (e: Exception) {
            Log.d("SwimspotsRepo", "Exception at getNearestSwimspots()")
            swimspots
                .subList(0, limit - 1)
        }
        Log.d("SwimspotsRepo", "Returning list of nearest swimspots: ${swimspotsSorted.size}")

        return swimspotsSorted
    }
}