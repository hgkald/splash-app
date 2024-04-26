package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.frostApi.FrostRepository
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastRepository
import no.uio.ifi.in2000.team22.badeapp.data.metalert.MetAlertRepository
import no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi.OceanforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi.OceanforecastRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.forecast.WaterTemperature
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimspotType

data class SwimspotUiState(
    val swimspot: Swimspot? = null
)

data class WeatherUiState(
    val alerts: List<Alert> = listOf(),
    val weather: Weather? = null,
    val water: WaterTemperature? = null,
)

class SwimspotViewModel(
    savedStateHandle: SavedStateHandle,
    swimspotsRepository: SwimspotsRepository
) : ViewModel() {
    private val swimspotId: String = checkNotNull(savedStateHandle.get<String>("swimspotId"))

    private val _swimspotUiState = MutableStateFlow(SwimspotUiState())
    private val _weatherUiState = MutableStateFlow(WeatherUiState())

    val swimSpotUiState: StateFlow<SwimspotUiState> = _swimspotUiState.asStateFlow()
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState.asStateFlow()


    private val alertRepo = MetAlertRepository()
    private val weatherRepo = LocationforecastRepository(LocationforecastDataSource())
    private val oceanRepo = OceanforecastRepository(OceanforecastDataSource())
    private val frostRepo = FrostRepository()

    init {
        viewModelScope.launch {
            _swimspotUiState.update {
                Log.d("SwimspotViewModel", "Updating swimspot ui state")
                val swimspot = swimspotsRepository.getSwimspotById(swimspotId)
                it.copy(
                    swimspot = swimspot
                )
            }

            _weatherUiState.update {
                val swimspot = _swimspotUiState.value.swimspot ?: return@update WeatherUiState()
                frostRepo.fetchWaterTemperature(swimspot.lat, swimspot.lon)


                it.copy(
                    alerts = alertRepo.getAlertsForPosition(
                        lat = swimspot.lat,
                        lon = swimspot.lon
                    ),
                    weather = weatherRepo.fetchCurrentWeather(
                        lat = swimspot.lat,
                        lon = swimspot.lon
                    ),
                    water = if (swimspot.type == SwimspotType.SALT) {
                        oceanRepo.fetchTemperature(
                            lat = swimspot.lat,
                            lon = swimspot.lon
                        )
                    } else {
                        frostRepo.fetchWaterTemperature(
                            lat = swimspot.lat,
                            lon = swimspot.lon
                        )
                    }
                )
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(swimspotsRepository: SwimspotsRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val savedStateHandle = extras.createSavedStateHandle()
                    return SwimspotViewModel(savedStateHandle, swimspotsRepository) as T
                }
            }
    }

}