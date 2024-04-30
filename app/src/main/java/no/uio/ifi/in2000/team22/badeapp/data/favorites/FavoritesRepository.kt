package no.uio.ifi.in2000.team22.badeapp.data.favorites

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.persistence.FavoritesDao

data class FavoritesState(
    val favorites: List<Favorite>,
)

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    val allFavorites: Flow<List<Favorite>> = favoritesDao.getAll()

    suspend fun isFavorite(id: Int): Boolean {
        val favorites = allFavorites.first()
        return favorites.any { it.id == id }
    }

    @WorkerThread
    suspend fun insert(id: Int) {
        favoritesDao.insert(Favorite(id))
    }

    @WorkerThread
    suspend fun delete(id: Int) {
        favoritesDao.delete(Favorite(id))
    }
}