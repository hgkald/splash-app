package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.FrostRepository
import no.uio.ifi.in2000.team22.badeapp.data.location.UserLocationRepository
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastRepository
import no.uio.ifi.in2000.team22.badeapp.data.metalert.MetAlertDataSource
import no.uio.ifi.in2000.team22.badeapp.data.metalert.MetAlertRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

data class SwimSpotUiState(
    val swimspotList: List<Swimspot> = emptyList<Swimspot>()// Swim Spot
)

data class WeatherUiState(
    val weather: Weather = Weather(
        time = null,
        airTemperature = null,
        symbolCode = null,
        windSpeed = null,
        windFromDirection = null,
        uvIndex = null,
        precipitationNextHour = null
    ),
    val metAlerts: List<Alert> = emptyList(),
    val weatherLocation: Point = Point.fromLngLat(10.7215, 59.9464)
)

data class MapUiState @OptIn(MapboxExperimental::class) constructor(
    val mapViewportState: MapViewportState = MapViewportState(),
    val homeLocation: Point = Point.fromLngLat(10.7215, 59.9464),
    val gpsLocationKnown: Boolean = false,
)

@OptIn(MapboxExperimental::class)

class HomeScreenViewModel(private val swimspotsRepository: SwimspotsRepository) : ViewModel() {
    private val _swimSpotUiState = MutableStateFlow(SwimSpotUiState())
    private val _weatherUiState = MutableStateFlow(WeatherUiState())
    @OptIn(MapboxExperimental::class)
    private val _mapUiState = MutableStateFlow(MapUiState())

    val swimSpotUiState: StateFlow<SwimSpotUiState> = _swimSpotUiState.asStateFlow()
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()
    val mapUiState: StateFlow<MapUiState> = _mapUiState.asStateFlow()

    val locationforecastRepo = LocationforecastRepository(LocationforecastDataSource())
    suspend fun getCurrentWeather(lat: Double, lon: Double): Weather {
        Log.i("HomeScreenViewModel", "getCurrentWeather() for location: lat: $lat, lon: $lon")
        return locationforecastRepo.fetchCurrentWeather(lat, lon)
    }

    val metAlertRepo = MetAlertRepository(MetAlertDataSource())
    suspend fun getMetAlerts(lat: Double, lon: Double): List<Alert> {
        return metAlertRepo.getAlertsForPosition(lat, lon)
    }

    fun isGpsLocationKnown(bool: Boolean) {
        _mapUiState.update {
            it.copy(
                gpsLocationKnown = bool
            )
        }
    }

    fun updateWeatherLocation(point: Point) {
        viewModelScope.launch {
            _weatherUiState.update {
                it.copy(
                    weatherLocation = point
                )
            }
        }
    }

    fun updateWeather(point: Point = _weatherUiState.value.weatherLocation) {
        viewModelScope.launch {
            _weatherUiState.update {
                it.copy(
                    weather = getCurrentWeather(point.latitude(), point.longitude()),
                    metAlerts = getMetAlerts(point.latitude(), point.longitude())
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            val lat = _mapUiState.value.homeLocation.latitude()
            val lon = _mapUiState.value.homeLocation.longitude()

            _swimSpotUiState.update {
                it.copy(swimspotList = swimspotsRepository.getAllSwimspots())
            }

            _mapUiState.update { state ->
                Log.i("HomeViewModel", "Updating map state")
                state.copy(
                    homeLocation = Point.fromLngLat(lon, lat),
                    mapViewportState = MapViewportState().apply {
                        setCameraOptions {
                            zoom(10.0)
                            center(state.homeLocation)
                            pitch(0.0)
                            bearing(0.0)
                        }
                    }
                )
            }

            _weatherUiState.update {
                it.copy(
                    weather = getCurrentWeather(lat, lon),
                    metAlerts = getMetAlerts(lat, lon),
                    weatherLocation = _mapUiState.value.homeLocation
                )
            }
        }

    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(swimspotsRepository: SwimspotsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return HomeScreenViewModel(swimspotsRepository) as T
                }
            }
    }

}