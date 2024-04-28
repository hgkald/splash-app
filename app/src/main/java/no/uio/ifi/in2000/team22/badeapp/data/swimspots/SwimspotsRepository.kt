package no.uio.ifi.in2000.team22.badeapp.data.swimspots

import android.content.Context
import android.location.Location
import android.util.Log
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

class SwimspotsRepository(private val context: Context) {
    private val datasource = SwimspotsDataSource
    private var swimspots = emptyList<Swimspot>()

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
     * Sort swimspots after distance to a given point, and return the x nearest swimspots
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
        Log.d("SwimspotsRepo", "beep getting nearest swimSpots")

        val swimspotsSorted = try {
            swimspots
                .map {
                    val results: FloatArray = floatArrayOf(0F)
                    Location.distanceBetween(latitude, longitude, it.lat, it.lon, results)

                    it.distance = results.first()
                    it
                    //Pair(it, distance)
                }
                .sortedBy {
                    it.distance
                }.subList(0, limit - 1)
        } catch (e: Exception) {
            Log.d("SwimspotsRepo", "Exception at getNearestSwimspots()")
            swimspots
                /*.map {
                    Pair(it, 0f)
                }
                .subList(0, limit - 1)*/

        }

        Log.d("SwimspotsRepo", "Returning list of nearest swimspots: ${swimspotsSorted.size}")

        return swimspotsSorted
    }
}