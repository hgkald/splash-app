package no.uio.ifi.in2000.team22.badeapp.data.favorites

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.persistence.FavoritesDao

data class FavoritesState(
    val favorites: List<Favorite>,
)

class FavoritesRepository(private val favoritesDao: FavoritesDao) {

    val allFavorites: Flow<List<Favorite>> = favoritesDao.getAll()

    @WorkerThread
    suspend fun insert(favorite: Favorite) {
        favoritesDao.insert(favorite)
    }

    @WorkerThread
    suspend fun delete(favorite: Favorite) {
        favoritesDao.delete(favorite)
    }
}