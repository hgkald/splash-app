package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.FrostRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot

data class SwimSpotUiState(
    val swimSpotList: List<SwimSpot> = emptyList<SwimSpot>()// Swim Spot
)

class HomeScreenViewModel() : ViewModel() {
    private val _swimSpotUiState = MutableStateFlow(SwimSpotUiState())
    val swimSpotUiState: StateFlow<SwimSpotUiState> = _swimSpotUiState.asStateFlow()
    val frostRepo = FrostRepository()

    suspend fun getAllSwimSpots(): List<SwimSpot> {
        return frostRepo.getAllSwimspots()
    }

    init {
        viewModelScope.launch {
            _swimSpotUiState.update {
                it.copy(swimSpotList = getAllSwimSpots())
            }
        }

    }

}