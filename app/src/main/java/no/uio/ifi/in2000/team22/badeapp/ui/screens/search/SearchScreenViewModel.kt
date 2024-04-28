package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesRepository
import no.uio.ifi.in2000.team22.badeapp.data.location.UserLocationRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite

data class SearchUiState(
    val swimspots: List<Swimspot> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
    val nearestSwimspots: List<Swimspot> = emptyList(),
    val searchInput: String = ""
)

data class LocationUiState(
    val lastKnownLocation: Location? = null,
    val locationPermissions: Boolean = false
)

class SearchScreenViewModel(
    private val swimspotsRepository: SwimspotsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val locationRepository: UserLocationRepository
) : ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())

    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()
    val locationUiState: StateFlow<LocationUiState> = locationRepository.observe()
        .map {
            LocationUiState(
                lastKnownLocation = it.lastKnownLocation,
                locationPermissions = it.permissionGranted
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocationUiState()
        )

    val filteredSwimspots: StateFlow<List<Swimspot>> = searchUiState
        .map { uiState ->
            uiState.nearestSwimspots.filter {
                it.name.startsWith(uiState.searchInput, ignoreCase = true)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            launch {
                favoritesRepository.allFavorites.collect { favoritesList ->
                    _searchUiState.update {
                        it.copy(
                            favorites = favoritesList
                        )
                    }
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

    fun setInput(newInput: String) {
        _searchUiState.update { currentState ->
            currentState.copy(searchInput = newInput)
        }
    }

    fun updateNearestSwimspots(lat: Double, lon: Double) {
        viewModelScope.launch {
            Log.d("SearchViewModel", "Latest location not null, continuing")
            _searchUiState.update {
                it.copy(
                    nearestSwimspots = swimspotsRepository.getNearestSwimspots(
                        latitude = lat,
                        longitude = lon
                    )
                )
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
        fun provideFactory(
            swimspotsRepository: SwimspotsRepository,
            favoritesRepository: FavoritesRepository,
            locationRepository: UserLocationRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchScreenViewModel(
                        swimspotsRepository,
                        favoritesRepository,
                        locationRepository
                    ) as T
                }
            }
    }

}