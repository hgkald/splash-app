package no.uio.ifi.in2000.team22.badeapp.data.favorites

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.persistence.FavoritesDao

//data class FavoritesState(
//    val favorites: List<Favorite>,
//)

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    val allFavorites: Flow<List<Favorite>> = favoritesDao.getAll()

    /**
     * Check if a given swimspot is also a favorite
     *
     * @param id the swimspot to check
     * @return returns a [Boolean] based on the result
     */
    suspend fun isFavorite(id: Int): Boolean {
        val favorites = allFavorites.first()
        return favorites.any { it.id == id }
    }

    /**
     * Inserts a favorite into the favorite database
     *
     * @param id of the swimspot to add
     */
    @WorkerThread
    suspend fun insert(id: Int) {
        favoritesDao.insert(Favorite(id))
    }

    /**
     * Removes a favorite from the favorite database
     *
     * @param id of the swimspot to remove
     */
    @WorkerThread
    suspend fun delete(id: Int) {
        favoritesDao.delete(Favorite(id))
    }
}