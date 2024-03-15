package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.FrostRepository
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastRepository
import no.uio.ifi.in2000.team22.badeapp.model.forecast.CurrentWeather
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot

data class SwimSpotUiState(
    val swimSpotList: List<SwimSpot> = emptyList<SwimSpot>()// Swim Spot
)

data class WeatherUiState(
    val currentWeather: CurrentWeather = CurrentWeather(
        time = null,
        airTemperature = null,
        symbolCode = null,
        windSpeed = null,
        windFromDirection = null,
        uvIndex = null,
        precipitationNextHour = null
    )
)

class HomeScreenViewModel : ViewModel() {
    private val _swimSpotUiState = MutableStateFlow(SwimSpotUiState())
    private val _weatherUiState = MutableStateFlow(WeatherUiState())
    val swimSpotUiState: StateFlow<SwimSpotUiState> = _swimSpotUiState.asStateFlow()
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()

    val frostRepo = FrostRepository()
    suspend fun getSwimSpots(lat: Double, lon: Double): List<SwimSpot> {
        return frostRepo.getBadeplasser(10.0, 5, lon, lat)
    }

    val locationforecastRepo = LocationforecastRepository(LocationforecastDataSource())
    suspend fun getCurrentWeather(lat:Double, lon: Double): CurrentWeather {
        return locationforecastRepo.fetchCurrentWeather(lat, lon)
    }

    suspend fun getAllSwimSpots(): List<SwimSpot> {
        return frostRepo.getAllSwimspots()
    }

    init {
        val lat = 59.9464
        val lon = 10.7215

        viewModelScope.launch {
            _swimSpotUiState.update {
                it.copy(swimSpotList = getSwimSpots(lon, lat))
            }
            _weatherUiState.update {
                it.copy(currentWeather = getCurrentWeather(lat, lon))
            }
        }

    }

}