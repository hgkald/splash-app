package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mapbox.maps.extension.style.expressions.dsl.generated.distance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesRepository
import no.uio.ifi.in2000.team22.badeapp.data.location.UserLocationRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimspotType
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite

data class SearchUiState(
    val swimspots: List<Swimspot> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
    val nearestSwimspots: List<Swimspot> = emptyList(),
    val searchInput: String = "",
    val freshwaterOnly: Boolean = false,
    val saltwaterOnly: Boolean = false
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
    private var previousLocation: Location? = null

    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()
    val locationUiState: StateFlow<LocationUiState> = locationRepository.observe()
        .map {
            LocationUiState(
                lastKnownLocation = it.lastKnownLocation,
                locationPermissions = it.permissionGranted
            )
        }
        .onEach { uiState ->
            Log.d("SearchScreenViewModel", uiState.lastKnownLocation.toString())
            viewModelScope.launch {
                uiState.lastKnownLocation?.let { updateNearestSwimspots(it.latitude, it.longitude) }
            }
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocationUiState()
        )

    val filteredSwimspots: StateFlow<List<Swimspot>> = searchUiState
        .map { uiState ->
            var result = uiState.swimspots

            if (uiState.searchInput.isNotBlank()) {
                result = result.filter {
                    it.name.startsWith(uiState.searchInput, ignoreCase = true)
                }
            }

            result = when {
                uiState.freshwaterOnly && uiState.saltwaterOnly -> {
                    result
                }
                uiState.freshwaterOnly -> {
                    result.filter { swimspot -> swimspot.type == SwimspotType.FRESH }
                }
                uiState.saltwaterOnly -> {
                    result.filter { swimspot -> swimspot.type == SwimspotType.SALT }
                }
                else -> {
                    result
                }
            }
            result.sortedBy { it.distance }
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
            launch {
                locationUiState.collect { uiState ->
                    Log.d("SearchScreenViewModel", "Updating nearest swimspots (${uiState})")
                    uiState.lastKnownLocation?.let { location ->
                        updateNearestSwimspots(location.latitude, location.longitude)
                    }
                }
            }
        }
    }

    fun setInput(newInput: String) {
        _searchUiState.update { currentState ->
            currentState.copy(searchInput = newInput)
        }
    }

    fun toggleFreshwaterOnly() {
        _searchUiState.update { currentState ->
            currentState.copy(freshwaterOnly = !_searchUiState.value.freshwaterOnly)
        }
    }

    fun toggleSaltwaterOnly() {
        _searchUiState.update { currentState ->
            currentState.copy(saltwaterOnly = !_searchUiState.value.saltwaterOnly)
        }
    }

    fun updateNearestSwimspots(lat: Double, lon: Double) {
        viewModelScope.launch {
            Log.d("SearchViewModel", "updateNearestSwimspots()")
            val nearestSwimspots = swimspotsRepository.getNearestSwimspots(lat, lon)
            _searchUiState.update {
                it.copy(
                    nearestSwimspots = nearestSwimspots
                )
            }
        }
    }

    fun addFavorite(id: Int) = viewModelScope.launch {
        Log.i("SearchScreenViewModel", "Adding favorite: $id to ${_searchUiState.value.favorites}")
        favoritesRepository.insert(id)
    }

    fun removeFavorite(id: Int) = viewModelScope.launch {
        Log.i("SearchScreenViewModel", "Removing favorite: $id")
        favoritesRepository.delete(id)
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