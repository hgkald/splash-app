package no.uio.ifi.in2000.team22.badeapp.data

import android.content.Context
import android.util.Log
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import java.io.BufferedReader
import java.io.InputStreamReader

class SwimspotDataSource (context: Context) {
    private val inp = InputStreamReader(context.assets.open("SwimspotList.csv"))
    private val scanner = BufferedReader(inp)
    var id = 0
    val list = mutableListOf<Swimspot>()

    fun lesFraFil() : MutableList<Swimspot> {
        scanner.readLines().forEach{
            val swimspot = it.split(";")
            id ++
            list.add(Swimspot(id, swimspot[0],swimspot[2].toDouble(), swimspot[1].toDouble()))
        }

        Log.d("SwimspotDataSource", list.toString())
        return list
    }

}
