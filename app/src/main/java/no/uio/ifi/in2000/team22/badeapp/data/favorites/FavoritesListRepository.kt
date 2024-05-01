package no.uio.ifi.in2000.team22.badeapp.data.favorites

import android.util.Log
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

class FavoritesListRepository{
    //private val favList = mutableListOf<Swimspot>()
    private val favList = mutableListOf(
        Swimspot(0, "Åkrasand", 59.250681, 5.194152),
        Swimspot(1, "Stavasand", 59.232681, 5.184657),
        Swimspot(2, "Fotvatnet", 59.298138, 5.286767),
        Swimspot(3, "Sandvesand",59.171176, 5.195650)
    )
    /*
    */

    fun addFavorite(swimspot: Swimspot){
        favList.add(swimspot)
        Log.d("FavRepo", "Swimspot added to favList[${favList.size}]: ${favList}")
    }

    fun removeFavorite(swimspot: Swimspot){
        favList.remove(swimspot)
        Log.d("FavRepo", "Swimspot removed from favList[${favList.size}]: ${favList}")
    }

    fun getFavList() : List<Swimspot>{
        return favList.toMutableList()
    }

}