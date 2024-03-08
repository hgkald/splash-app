package no.uio.ifi.in2000.team22.badeapp.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.Badested
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.FrostRepository

data class SwimSpotUiState(
    val swimSpotList: List<Badested> = emptyList<Badested>()// Swim Spot
)

class MapScreenViewModel (): ViewModel() {
    private val _swimSpotUiState = MutableStateFlow(SwimSpotUiState())
    val swimSpotUiState: StateFlow<SwimSpotUiState> = _swimSpotUiState.asStateFlow()
    val frostRepo = FrostRepository()

    suspend fun getSwimSpots(lat : Double, lon : Double) : List<Badested> {
        return frostRepo.getBadeplasser(10.0, 5, lon, lat)


    }
    init {
        viewModelScope.launch {
            _swimSpotUiState.update {
                it.copy(swimSpotList = getSwimSpots( lon = 10.7215, lat = 59.9464))
            }
        }

    }

}