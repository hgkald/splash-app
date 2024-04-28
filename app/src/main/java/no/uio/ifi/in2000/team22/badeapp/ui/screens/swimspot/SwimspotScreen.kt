package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.loading.LoadingIndicator
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.AlertOverview
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.weatherIconDrawable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwimspotScreen(
    navController: NavController,
    swimspotViewModel: SwimspotViewModel
) {
    val swimspotState: State<SwimspotUiState> = swimspotViewModel.swimSpotUiState.collectAsState()
    val weatherState: State<WeatherUiState> = swimspotViewModel.weatherUiState.collectAsState()
    val transportUiState: State<TransportUiState> =
        swimspotViewModel.transportUiState.collectAsState()

    val topBarScroll = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        text = "${swimspotState.value.swimspot?.name}",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Gå tilbake til forrige side"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = "Se hvor badeplassen ligger i kartet"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Legg til badeplassen som favoritt"
                        )
                    }
                },
                scrollBehavior = topBarScroll
            )
        },
        bottomBar = { BadeAppBottomAppBar(navController, "swimspot") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 10.dp, end = 10.dp, top = 2.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        )
        {
            Spacer(modifier = Modifier.height(20.dp))
            AlertOverview(weatherState.value.alerts)

            SubTitleDivider(title = "Kollektivtrafikk")
            TransportOverview(stops = transportUiState.value.stops)

            SubTitleDivider(title = "Vær og temperatur")
            WeatherOverview(
                waterTemp = weatherState.value.water,
                airTemp = weatherState.value.weather?.airTemperature,
                uvIndex = weatherState.value.weather?.uvIndex,
                precipitation = weatherState.value.weather?.precipitationNextHour,
                weatherIcon = weatherIconDrawable[weatherState.value.weather?.symbolCode]
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SwimspotOverview(swimspotState: State<SwimspotUiState>, weatherState: State<WeatherUiState>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = rememberLazyGridState(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .sizeIn(maxHeight = 600.dp)
            .fillMaxWidth()
    ) {
        item(span = {
            GridItemSpan(maxLineSpan)
        }) {
            WaterTempInfo(temperature = weatherState.value.water)
        }

        // --- Weather info ---

        val weather = weatherState.value.weather
        if (weather != null) {
            val textWeatherValues = listOf(Pair("${weather.airTemperature}", "Luftemperatur"))


            item(span = {
                GridItemSpan(1)
            }) {
                InfoCardImage(
                    id = R.drawable.partlycloudy_day,
                    contentDescription = "partlycloudy day",
                    label = "Værforhold"
                )
            }

            item(span = {
                GridItemSpan(1)
            }) {
                InfoCard(text = "16°", label = "Lufttemperatur")
            }

            item(span = {
                GridItemSpan(1)
            }) {
                InfoCard(text = "3", label = "Lavt UV-nivå")
            }

            item(span = {
                GridItemSpan(1)
            }) {
                InfoCard(text = "0.5mm", label = "Nedbør neste timen")
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
    }
}


@Composable
fun LargeInfoCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
) {
    val titleSize = if (LocalConfiguration.current.screenWidthDp < 360) {
        MaterialTheme.typography.headlineMedium
    } else {
        MaterialTheme.typography.headlineLarge
    }

    Card(
        modifier = modifier
            .sizeIn(maxWidth = 160.dp, minHeight = 110.dp)
            .height(intrinsicSize = IntrinsicSize.Min)
            .clickable(enabled = clickable, onClick = onClick)
            .fillMaxSize(),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .weight(0.75f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = text,
                    style = titleSize,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Box(modifier = modifier.weight(0.25f)) {
                Text(
                    text = label,
                    textAlign = TextAlign.Center,
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
            .sizeIn(maxWidth = 160.dp, minHeight = 110.dp)
            .height(intrinsicSize = IntrinsicSize.Min)
            .clickable(enabled = clickable, onClick = onClick)
            .fillMaxSize()
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
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
fun SubTitleDivider(title: String) {
    Column {
//        Divider(
//            modifier = Modifier.padding(top = 15.dp, bottom = 10.dp)
//        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 25.dp, bottom = 5.dp)
        )
    }
}