package no.uio.ifi.in2000.team22.badeapp.data.favorites

import android.util.Log
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

class FavoritesListRepositoryOld (private val favList : MutableList<Swimspot> = mutableListOf()){
    //private var favList = emptyList<Swimspot>()

    init {
        favList.addAll(
            mutableListOf(
                Swimspot(0, "Åkrasand", 59.250681, 5.194152),
                Swimspot(1, "Stavasand", 59.232681, 5.184657),
                Swimspot(2, "Fotvatnet", 59.298138, 5.286767),
                Swimspot(3, "Sandvesand",59.171176, 5.195650 )
            )
        )
    }


    fun getFavorites() : List<Swimspot> {
        return favList
    }

    fun addFavorite(swimspot: Swimspot){
        favList.add(swimspot)
        Log.d("FavoritesListRepository", "Favorited $swimspot")
    }

    fun removeFavorite(swimspot: Swimspot){
        favList.remove(swimspot)
        Log.d("FavoritesListRepository", "Unfavorited $swimspot")
    }

}