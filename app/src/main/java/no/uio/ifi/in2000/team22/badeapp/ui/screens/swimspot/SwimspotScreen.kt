package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.weatherIconDrawable

@Composable
fun SwimspotScreen(
    navController: NavController,
    swimspotViewModel: SwimspotViewModel = viewModel()
) {
    val state: State<SwimspotUiState> = swimspotViewModel.swimSpotUiState.collectAsState()

    Scaffold(
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        )
        {
            SwimspotOverview(state = state)
        }
    }
}

@Composable
fun SwimspotOverview(state: State<SwimspotUiState>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Sørenga",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        AlertOverview(state.value.alerts)
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            WeatherOverview(
                waterTemp = state.value.ocean?.waterTemperature,
                airTemp = state.value.weather?.airTemperature,
                uvIndex = state.value.weather?.uvIndex,
                weatherIcon = weatherIconDrawable[state.value.weather?.symbolCode]
            )
        }
    }
}