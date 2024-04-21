package no.uio.ifi.in2000.team22.badeapp.data.swimspots

import android.content.Context
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
}