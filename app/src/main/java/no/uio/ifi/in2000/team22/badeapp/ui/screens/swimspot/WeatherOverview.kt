package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.model.forecast.WaterTemperature
import no.uio.ifi.in2000.team22.badeapp.model.uv.doubleToUV
import no.uio.ifi.in2000.team22.badeapp.model.uv.uvToNorwegian
import no.uio.ifi.in2000.team22.badeapp.ui.components.loading.LoadingIndicator
import java.time.Duration
import java.time.Instant

@Preview(showSystemUi = true)
@Composable
fun WeatherOverviewPreview() {
    WeatherOverview(
        waterTemp = WaterTemperature(20.0, Instant.now()),
        airTemp = 21.0,
        uvIndex = 3.0,
        precipitation = 0.5,
        weatherIcon = R.drawable.partlycloudy_day
    )
}

@Composable
fun WeatherOverview(
    waterTemp: WaterTemperature?,
    airTemp: Double?,
    uvIndex: Double?,
    precipitation: Double?,
    @DrawableRes weatherIcon: Int?,
) {
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
            WaterTempInfo(temperature = waterTemp)
        }

        if (airTemp != null && uvIndex != null && precipitation != null) {
            val uv = doubleToUV(uvIndex)
            val textWeatherValues = listOf(
                Pair("${airTemp}°", "Luftemperatur"),
                Pair("${uvIndex}", "${uvToNorwegian(uv)} UV-nivå"),
                Pair("${precipitation}mm", "Nedbør neste timen")
            )


            item(span = {
                GridItemSpan(1)
            }) {
                if (weatherIcon != null) {
                    InfoCardImage(
                        id = weatherIcon,
                        contentDescription = "partlycloudy day",
                        label = "Værforhold"
                    )
                }
            }

            textWeatherValues.map {
                item(span = { GridItemSpan(1) }
                ) {
                    InfoCard(text = it.first, label = it.second)
                }
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
fun WaterTempInfo(temperature: WaterTemperature?) {
    LargeInfoCard {
        if (temperature != null) {
            Text(
                text = "${temperature.temperature}° i vannet",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )

            val timeSince = Duration.between(temperature.time, Instant.now()).toDays()
            Text(
                text = "Sist målt $timeSince dager siden",
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            LoadingIndicator(onErrorText = "Ingen registrerte badetemperaturer for dette badestedet")
        }

    }
}

@Composable
fun WideInfoCard(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .height(110.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            content()
        }
    }
}