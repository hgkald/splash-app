package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite

data class FavoritesUiState(
    val favorites: List<Swimspot?>,
    val swimspots: List<Swimspot>
)

class FavoritesViewModel(
    private val repository: FavoritesRepository,
    private val swimspotsRepository: SwimspotsRepository
) : ViewModel() {

    private val _favoritesUiState = MutableStateFlow(FavoritesUiState(emptyList(), emptyList()))
    val favoritesUiState: StateFlow<FavoritesUiState> = _favoritesUiState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                val swimspots = swimspotsRepository.getAllSwimspots()
                _favoritesUiState.update {
                    it.copy(swimspots = swimspots)
                }
            }
            launch {
                repository.allFavorites.collect { favoritesList ->
                    val swimspots = favoritesList.map { favorite ->
                        swimspotsRepository.getSwimspotById(favorite.id.toString())
                    }
                    _favoritesUiState.update {
                        it.copy(favorites = swimspots)
                    }
                }
            }
        }
    }

    fun insert(id: Int) = viewModelScope.launch {
        Log.i("favoritesViewModel", "Adding favorite: $id to ${_favoritesUiState.value.favorites}")
        repository.insert(id)
    }

    fun delete(id: Int) = viewModelScope.launch {
        Log.i("favoritesViewModel", "Removing favorite: $id")
        repository.delete(id)
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            favoritesRepository: FavoritesRepository,
            swimspotsRepository: SwimspotsRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return FavoritesViewModel(favoritesRepository, swimspotsRepository) as T
                }
            }
    }
}
