package no.uio.ifi.in2000.team22.badeapp.data.enTur

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StopUIState(
    val stoppList : List<StopPlace> = emptyList()
)
class EnTurViewModel : ViewModel() {

    private val repo: EnTurRepository = EnTurRepository()
    private val _stops = MutableStateFlow(StopUIState())
    val stops: StateFlow<StopUIState> = _stops.asStateFlow()

    suspend fun getStops(lat: Double, lon: Double): List<StopPlace> {
        return repo.getStops(lat, lon, 20, 5)
    }

    init {
        viewModelScope.launch {
            _stops.update {
                it.copy(stoppList = getStops( 59.90114413151885, 10.752138169308529,))
            }
        }
    }
}



