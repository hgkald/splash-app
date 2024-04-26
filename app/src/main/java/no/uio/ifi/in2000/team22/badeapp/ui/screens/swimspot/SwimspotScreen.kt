package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.loading.LoadingIndicator
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.AlertOverview

@Composable
fun SwimspotScreen(
    navController: NavController,
    swimspotViewModel: SwimspotViewModel
) {
    val swimspotState: State<SwimspotUiState> = swimspotViewModel.swimSpotUiState.collectAsState()
    val weatherState: State<WeatherUiState> = swimspotViewModel.weatherUiState.collectAsState()

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
            SwimspotOverview(
                swimspotState = swimspotState,
                weatherState = weatherState
            )
        }
    }


}

@Composable
fun SwimspotOverview(swimspotState: State<SwimspotUiState>, weatherState: State<WeatherUiState>) {
    Column(
        modifier = Modifier
//            .verticalScroll(rememberScrollState())
            .padding(10.dp)
            .fillMaxWidth()
    ) {

        val waterTemperatureLoaded = (weatherState.value.water == null)

        Text(
            text = "${swimspotState.value.swimspot?.name}",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 14.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                AlertOverview(weatherState.value.alerts)
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                SubTitleDivider(title = "Vær og temperatur")
            }

            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                WideInfoCard {
                    if (waterTemperatureLoaded) {
                        Text(
                            text = "12° i vannet",
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            text = "Sist målt 2 dager siden",
                            style = MaterialTheme.typography.bodySmall
                        )
                    } else {
                        LoadingIndicator(onErrorText = "Ingen registrerte badetemperaturer for dette badestedet")
                    }

                }
            }

            val weather = weatherState.value.weather
            if (weather != null) {
                val textWeatherValues = listOf(Pair("${weather.airTemperature}", "Luftemperatur"))


                item(span = {
                    GridItemSpan(2)
                }) {
                    InfoCardImage(
                        id = R.drawable.partlycloudy_day,
                        contentDescription = "partlycloudy day",
                        label = "Værforhold"
                    )
//                    SmallInfoCard(verticalArrangement = Arrangement.Center) {
//                        Image(
//                            painter = painterResource(id = R.drawable.partlycloudy_day),
//                            contentDescription = "partlycloudy day",
//                            modifier = Modifier.weight(0.5f, fill = true)
//                        )
//                        Text(
//                            text = "Værforhold",
//                            style = MaterialTheme.typography.labelSmall,
//                            modifier = Modifier.weight(0.15f, fill = true)
//                        )
//                    }
                }

                item(span = {
                    GridItemSpan(2)
                }) {
                    InfoCard(text = "16°", label = "Lufttemperatur")
//                    SmallInfoCard(
//                        verticalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Text(
//                            text = "16°",
//                            style = MaterialTheme.typography.displayMedium
//                        )
//                        Text(
//                            text = "Lufttemperatur",
//                            style = MaterialTheme.typography.labelSmall,
//                        )
//                    }
                }

                item(span = {
                    GridItemSpan(2)
                }) {
                    InfoCard(text = "3", label = "Lavt UV-nivå")
//                    SmallInfoCard(
//                        verticalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Text(
//                            text = "3",
//                            style = MaterialTheme.typography.displayMedium,
//                        )
//                        Text(
//                            text = "Lavt UV-nivå",
//                            style = MaterialTheme.typography.labelSmall,
//                        )
//
//                    }
                }

                item(span = {
                    GridItemSpan(2)
                }) {
                    InfoCard(text = "0.5mm", label = "Nedbør neste timen")
//                    SmallInfoCard(
//                        verticalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Text(
//                            text = "0.5mm",
//                            style = MaterialTheme.typography.displayMedium,
//                        )
//                        Text(
//                            text = "Nedbør neste timen",
//                            style = MaterialTheme.typography.labelSmall,
//                        )
//                    }
                }
            } else {
                item(span = {
                    GridItemSpan(maxLineSpan)
                }) {
                    WideInfoCard {
                        LoadingIndicator(onErrorText = "Kunne ikke hente værinformasjon for badestedet")
                    }
                }
            }



            item(span = {
                GridItemSpan(maxLineSpan)
            }) {
                SubTitleDivider(title = "Kollektivtilbud i nærheten")
            }
        }

//        AlertOverview(weatherState.value.alerts)

//        Spacer(modifier = Modifier.height(20.dp))
//
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            WeatherOverview(
//                waterTemp = weatherState.value.water?.temperature,
//                waterTempTime = weatherState.value.water?.time,
//                airTemp = weatherState.value.weather?.airTemperature,
//                uvIndex = weatherState.value.weather?.uvIndex,
//                weatherIcon = weatherIconDrawable[weatherState.value.weather?.symbolCode]
//            )
//        }
    }
}

fun WideInfoCard() {

}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .sizeIn(maxHeight = 110.dp, maxWidth = 110.dp)
            .clickable(enabled = clickable, onClick = onClick)
            .fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = modifier.weight(0.75f)) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Box(modifier = modifier.weight(0.25f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
fun InfoCardImage(
    modifier: Modifier = Modifier,
    @DrawableRes id: Int,
    contentDescription: String,
    label: String,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .sizeIn(maxHeight = 110.dp, maxWidth = 110.dp)
            .clickable(enabled = clickable, onClick = onClick)
            .fillMaxWidth(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = modifier.weight(0.75f)) {
                Image(
                    painter = painterResource(id = id),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            Box(modifier = modifier.weight(0.25f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
fun SubTitleDivider(title: String) {
    Column {
        Divider(
            modifier = Modifier.padding(top = 15.dp, bottom = 10.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )
    }
}