package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.location.UserLocationRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

data class SearchUiState(
    val swimspots: List<Swimspot> = emptyList(),
    val nearestSwimspots: List<Pair<Swimspot, Float>> = emptyList()
)

data class LocationUiState(
    val lastKnownLocation: Location? = null,
    val locationPermissions: Boolean = false
)

class SearchScreenViewModel(
    private val swimspotsRepository: SwimspotsRepository,
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

    fun updateSuggestions(lat: Double, lon: Double) {
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

    init {
        viewModelScope.launch {
            _searchUiState.update {
                it.copy(swimspots = swimspotsRepository.getAllSwimspots())
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            swimspotsRepository: SwimspotsRepository,
            locationRepository: UserLocationRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchScreenViewModel(swimspotsRepository, locationRepository) as T
                }
            }
    }

}