package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.team22.badeapp.data.metalert.Alert
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot
import no.uio.ifi.in2000.team22.badeapp.ui.screens.home.SwimSpotUiState

data class SwimSpotUiState(
    val swimSpot: SwimSpot,
    val alert: Alert
    //TODO: Add locationforecast
)

class SwimSpotViewModel() : ViewModel() {
    private val _swimSpotUiState = MutableStateFlow(SwimSpotUiState())
    val swimSpotUiState: StateFlow<SwimSpotUiState> = _swimSpotUiState.asStateFlow()
}