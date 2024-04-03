package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.weatherIconDrawable

@Composable
fun SwimspotScreen(
    navcontroller: NavController,
    swimspotViewModel: SwimspotViewModel = viewModel()
) {
    val state: State<SwimspotUiState> = swimspotViewModel.swimSpotUiState.collectAsState()

    Scaffold(
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar(navcontroller) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (state.value.swimspot == null
                || state.value.weather == null
            ) {
                LoadingIndicator()
            } else {
                SwimspotOverview(state)
            }
        }
    }
}

@Composable
fun SwimspotOverview(state: State<SwimspotUiState>) {
    val currentWeatherDrawable = weatherIconDrawable[state.value.weather?.symbolCode]

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "${state.value.swimspot?.name}",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(bottom = 15.dp)
        )
        InfoCardCollection(title = "Temperatur og vær") {
            InfoCardText(
                temperature = getTemperature(state.value.weather?.airTemperature),
                subtext = "i luften"
            )
            InfoCardText(
                temperature = getTemperature(state.value.weather?.airTemperature),
                subtext = "i vannet"
            )
            if (currentWeatherDrawable != null) {
                InfoCardImage(
                    painter = painterResource(id = currentWeatherDrawable),
                    contentDescription = "clear sky",
                    subtext = "akkurat nå"
                )
            }
        }
    }
}

@Preview
@Composable
fun InfoCardCollectionPreview() {
    InfoCardCollection(title = "Temperatur og vær") {
        InfoCardText(temperature = "20", subtext = "akkurat nå")
        InfoCardText(temperature = "20", subtext = "akkurat nå")
        InfoCardText(temperature = "20", subtext = "akkurat nå")
    }
}

@Preview
@Composable
fun InfoImagePreview() {
    InfoCardImage("akkurat nå", painterResource(id = R.drawable.rain), contentDescription = "test")
}

@Preview
@Composable
fun InfoCardTextPreview() {
    InfoCardText(temperature = "20", subtext = "i vannet")
}

@Composable
fun InfoCardCollection(
    title: String,
    content: @Composable() () -> Unit,
) {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Divider(
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
        Divider(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )
    }
}

@Composable
fun InfoCardText(temperature: String, subtext: String) {
    Card(
        modifier = Modifier
            .size(110.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                "$temperature°",
                style = MaterialTheme.typography.displayMedium,
            )
            Text(
                subtext,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun InfoCardImage(subtext: String, painter: Painter, contentDescription: String) {
    Card(
        modifier = Modifier
            .size(110.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                subtext,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

private fun getTemperature(temp: Double?): String {
    return if (temp == null) {
        "null"
    } else {
        "${Math.round(temp)}"
    }
}