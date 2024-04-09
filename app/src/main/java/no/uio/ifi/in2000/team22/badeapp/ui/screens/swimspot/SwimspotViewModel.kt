package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.locationforecastApi.LocationforecastRepository
import no.uio.ifi.in2000.team22.badeapp.data.metalert.MetAlertRepository
import no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi.OceanforecastDataSource
import no.uio.ifi.in2000.team22.badeapp.data.oceanforecastApi.OceanforecastRepository
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.forecast.OceanForecast
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

data class SwimspotUiState(
    val swimspot: Swimspot?,
    val alerts: List<Alert> = listOf(),
    val weather: Weather?,
    val ocean: OceanForecast?
)

class SwimspotViewModel() : ViewModel() {
    private val _swimspotUiState =
        MutableStateFlow(
            SwimspotUiState(
                swimspot = null,
                weather = null,
                ocean = null
            )
        )
    val swimSpotUiState: StateFlow<SwimspotUiState> = _swimspotUiState.asStateFlow()

    private val alertRepo = MetAlertRepository()
    private val weatherRepo = LocationforecastRepository(LocationforecastDataSource())
    private val oceanRepo = OceanforecastRepository(OceanforecastDataSource())
    private val lon = 10.75033
    private val lat = 59.90075

    init {
        viewModelScope.launch {
            _swimspotUiState.update {
                Log.d("SwimspotViewModel", "Updating swimspot ui state")
                it.copy(
                    swimspot = Swimspot(name = "Sørenga", lon = lon, lat = lat),
                    alerts = alertRepo.getAlertsForPosition(lon = lon, lat = lat),
                    weather = weatherRepo.fetchCurrentWeather(lon = lon, lat = lat),
                    ocean = oceanRepo.fetchTemperature(lon = lon, lat = lat)
                )

            }
        }
    }

}