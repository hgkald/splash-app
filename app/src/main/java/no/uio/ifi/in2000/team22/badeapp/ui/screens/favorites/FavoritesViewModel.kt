package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite

data class FavoritesUiState(
    val favorites: List<Swimspot?>,
//    val swimspots: List<Swimspot>
)

class FavoritesViewModel(
    private val repository: FavoritesRepository,
    private val swimspotsRepository: SwimspotsRepository
) : ViewModel() {

    private val _favoritesUiState = MutableStateFlow(FavoritesUiState(emptyList()))//, emptyList()))
    //val favoritesUiState: StateFlow<FavoritesUiState> = _favoritesUiState.asStateFlow()
    lateinit var favoritesUiState: StateFlow<FavoritesUiState>

    init {
        viewModelScope.launch {
            favoritesUiState = repository.observe()
                .map { favorites ->
                    favorites.map { favorite ->
                        swimspotsRepository.getSwimspotById(favorite.id.toString())
                    }
                }.map {
                    FavoritesUiState(
                        favorites = it,
                    )
                }.stateIn(
                    viewModelScope,
                    started = SharingStarted.Eagerly,
                    initialValue = FavoritesUiState(emptyList())//, emptyList())
                )
        }
    }
    /*
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
                    _favoritesUiState.update {
                        it.copy(favorites = favoritesList)
                    }
                }
            }
        }
    }*/

    fun insert(favorite: Favorite) = viewModelScope.launch {
        Log.i("favoritesViewModel", "Adding favorite: $favorite to ${_favoritesUiState.value.favorites}")
        repository.insert(favorite)
    }

    fun delete(favorite: Favorite) = viewModelScope.launch {
        Log.i("favoritesViewModel", "Removing favorite: $favorite")
        repository.delete(favorite)
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
