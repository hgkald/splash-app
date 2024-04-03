package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.FrostRepository
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.SwimSpotOverviewRepository
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastRepository
import no.uio.ifi.in2000.team22.badeapp.data.metalert.MetAlertDataSource
import no.uio.ifi.in2000.team22.badeapp.data.metalert.MetAlertRepository
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

data class SwimSpotUiState(
    val swimSpotList: List<Swimspot> = emptyList<Swimspot>()// Swim Spot
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
)

@OptIn(MapboxExperimental::class)
data class MapUiState(
    val mapViewportState: MapViewportState = MapViewportState(),
    val mapPointsState: List<PointAnnotationOptions> = listOf(),
    val mapPositionState: Point = Point.fromLngLat(10.7215, 59.9464)
)

@OptIn(MapboxExperimental::class)
class HomeScreenViewModel : ViewModel() {
    private val _swimSpotUiState = MutableStateFlow(SwimSpotUiState())
    private val _weatherUiState = MutableStateFlow(WeatherUiState())
    private val _mapUiState = MutableStateFlow(MapUiState())

    val swimSpotUiState: StateFlow<SwimSpotUiState> = _swimSpotUiState.asStateFlow()
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()
    val mapUiState: StateFlow<MapUiState> = _mapUiState.asStateFlow()

    val frostRepo = FrostRepository()
    suspend fun getSwimSpots(lat: Double, lon: Double): List<Swimspot> {
        return frostRepo.getBadeplasser(10.0, 5, lon, lat)
    }

    val locationforecastRepo = LocationforecastRepository(LocationforecastDataSource())
    suspend fun getCurrentWeather(lat: Double, lon: Double): Weather {
        return locationforecastRepo.fetchCurrentWeather(lat, lon)
    }

    val metAlertRepo = MetAlertRepository(MetAlertDataSource())
    suspend fun getMetAlerts(lat: Double, lon: Double): List<Alert> {
        return metAlertRepo.getAlertsForPosition(lat, lon)
    }

    suspend fun getAllSwimSpots(): List<Swimspot> {
        return frostRepo.getAllSwimspots()
    }

    fun updatePosition(point: Point) {
        viewModelScope.launch {
            _mapUiState.update {
                it.copy(
                    mapViewportState = MapViewportState().apply {
                        setCameraOptions {
                            zoom(10.0)
                            center(point)
                            pitch(0.0)
                            bearing(0.0)
                        }
                    }
                )
            }
        }
    }

    init {
        val lat = 59.9464
        val lon = 10.7215

        viewModelScope.launch {
            _swimSpotUiState.update {
                it.copy(swimSpotList = SwimSpotOverviewRepository.swimSpots)
            }

            _mapUiState.update { state ->
                Log.i("HomeViewModel", "Updating map state")
                state.copy(
                    mapPositionState = Point.fromLngLat(lon, lat),
                    mapPointsState = SwimSpotOverviewRepository.swimSpots.map {
                        PointAnnotationOptions()
                            .withPoint(
                                Point.fromLngLat(it.lon, it.lat)
                            )
                    },
                    mapViewportState = MapViewportState().apply {
                        setCameraOptions {
                            zoom(10.0)
                            center(state.mapPositionState)
                            pitch(0.0)
                            bearing(0.0)
                        }
                    }
                )
            }

            _weatherUiState.update {
                it.copy(
                    weather = getCurrentWeather(lat, lon),
                    metAlerts = getMetAlerts(lat, lon)
                )
            }
        }

    }

}