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
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

data class SwimspotUiState(
    val swimspot: Swimspot?,
    val alerts: List<Alert> = listOf(),
    val weather: Weather?
)

class SwimspotViewModel() : ViewModel() {
    private val _swimspotUiState =
        MutableStateFlow(
            SwimspotUiState(
                swimspot = null,
                weather = null
            )
        )
    val swimSpotUiState: StateFlow<SwimspotUiState> = _swimspotUiState.asStateFlow()

    val alertRepo = MetAlertRepository()
    val weatherRepo = LocationforecastRepository(LocationforecastDataSource())

    init {
        viewModelScope.launch {
            _swimspotUiState.update {
                Log.d("SwimspotViewModel", "Updating swimspot ui state")
                it.copy(
                    swimspot = Swimspot(name = "Sørenga", lat = 10.75033, lon = 59.90075),
//                    alerts = alertRepo.getAlertsForPosition(lat = 10.75033, lon = 59.90075),
//                    weather = weatherRepo.fetchCurrentWeather(lon = 10.75033, lat = 59.90075)
                )
            }
        }
    }

}