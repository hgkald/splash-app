package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite

data class SearchUiState(
    val swimspots: List<Swimspot> = emptyList(),
    val favorites: List<Favorite> = emptyList()
)

class SearchScreenViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val swimspotsRepository: SwimspotsRepository
) : ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    init {
        viewModelScope.launch {
            launch {
                _searchUiState.update {
                    it.copy(
                        favorites = favoritesRepository.observe()
                    )
                }
            }
            launch {
                val swimspots = swimspotsRepository.getAllSwimspots()
                _searchUiState.update { currentState ->
                    currentState.copy(swimspots = swimspots)
                }
            }
        }
    }

    fun addFavorite(favorite: Favorite) = viewModelScope.launch {
        Log.i("SearchScreenViewModel", "Adding favorite: $favorite to ${_searchUiState.value.favorites}")
        favoritesRepository.insert(favorite)
    }

    fun removeFavorite(favorite: Favorite) = viewModelScope.launch {
        Log.i("SearchScreenViewModel", "Removing favorite: $favorite")
        favoritesRepository.delete(favorite)
    }
    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(favoritesRepository: FavoritesRepository, swimspotsRepository: SwimspotsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchScreenViewModel(favoritesRepository, swimspotsRepository) as T
                }
            }
    }

}