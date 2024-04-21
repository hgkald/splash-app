package no.uio.ifi.in2000.team22.badeapp.data.swimspots

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimspotType
import java.io.BufferedReader
import java.io.InputStreamReader


/**
 * Reads a list of [Swimspot]s from disk. Implemented as object to make disk read ops less frequent.
 */
object SwimspotsDataSource {

    /**
     * Check if [swimspots] is empty or not, then reads [Swimspot]s from disk if [swimspots] is empty.
     *
     * @param context The current application context, only available in a composable
     * @return A list of [Swimspot]. Either from disk or from memory.
     */
    suspend fun getSwimspots(context: Context): List<Swimspot> {
        return withContext(Dispatchers.IO) {
            val input = InputStreamReader(context.assets.open("SwimspotList.csv"))
            val reader = BufferedReader(input)

            var id = 0
            val swimspots = reader.lineSequence().map {
                val values =
                    it.split(",", limit = 6) // This will not include the list of search words
                Swimspot(
                    id = id++,
                    name = values[0],
                    lat = values[1].toDouble(),
                    lon = values[2].toDouble(),
                    type = SwimspotType.fromString(values[3]),
                    municipality = values[4],
                    county = values[5]

                )

            }.toList()

            reader.close()

            return@withContext swimspots
        }
    }

//    suspend fun getSwimspots(context: Context): List<Swimspot> {
//        if (swimspots.isNotEmpty()) {
//            return swimspots
//        }
//
//        swimspots = withContext(Dispatchers.IO) {
//            val inp = InputStreamReader(context.assets.open("SwimspotList.csv"))
//            val scanner = BufferedReader(inp)
//            var id = 0
//            val list = mutableListOf<Swimspot>()
//
//
//            scanner.readLines().forEach {
//                val swimspot = it.split(",")
//                id++
//                list.add(Swimspot(id, swimspot[0], swimspot[2].toDouble(), swimspot[1].toDouble()))
//            }
//
//            Log.d("SwimspotsDataSource", "Reading swimspots from file")
//            return@withContext list
//        }
//
//        return swimspots
//    }
}

//class SwimspotsDataSource(context: Context) {
//    private val inp = InputStreamReader(context.assets.open("SwimspotList.csv"))
//    private val scanner = BufferedReader(inp)
//    var id = 0
//    val list = mutableListOf<Swimspot>()
//
//    fun lesFraFil(): MutableList<Swimspot> {
//        scanner.readLines().forEach {
//            val swimspot = it.split(";")
//            id++
//            list.add(Swimspot(id, swimspot[0], swimspot[2].toDouble(), swimspot[1].toDouble()))
//        }
//
//        Log.d("SwimspotDataSource", list.toString())
//        return list
//    }
//}
