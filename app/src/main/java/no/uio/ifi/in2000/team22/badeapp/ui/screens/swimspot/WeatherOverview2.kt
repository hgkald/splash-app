package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.model.uv.UV
import no.uio.ifi.in2000.team22.badeapp.model.uv.doubleToUV
import no.uio.ifi.in2000.team22.badeapp.model.uv.uvToNorwegian
import no.uio.ifi.in2000.team22.badeapp.ui.components.loading.LoadingIndicator
import java.time.Duration
import java.time.Instant
import kotlin.math.roundToInt

@Preview(showSystemUi = true)
@Composable
fun WeatherOverviewPreview() {
    WeatherOverview(
        waterTemp = 20.0,
        waterTempTime = Instant.now(),
        airTemp = 21.0,
        uvIndex = 2.0,
        weatherIcon = R.drawable.partlycloudy_day
    )
}

@Composable
fun WeatherOverview(
    waterTemp: Double?,
    waterTempTime: Instant?,
    airTemp: Double?,
    uvIndex: Double?,
    @DrawableRes weatherIcon: Int?,
) {
    Column(
        modifier = Modifier.widthIn(max = 400.dp)
    ) {
        Content(
            waterTemp = waterTemp,
            waterTempTime = waterTempTime,
            airTemp = airTemp,
            uvIndex = uvIndex,
            weatherIcon = weatherIcon
        )
    }
}


/**
 * Main content for [WeatherOverview]. Containts the main weather info.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    waterTemp: Double?,
    waterTempTime: Instant?,
    airTemp: Double?,
    uvIndex: Double?,
    @DrawableRes weatherIcon: Int?,

    ) {
    val showDialogStateUV = remember { mutableStateOf(false) }
    val uvValue = if (uvIndex != null) {
        doubleToUV(uvIndex)
    } else {
        UV.UNKNOWN
    }

    Text(
        text = "Vær og temperatur",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 9.dp)
    )
    Divider(
        modifier = Modifier.padding(bottom = 9.dp)
    )
    WideInfoCard {
        if (waterTemp == null || waterTempTime == null) {
            LoadingIndicator(
                onErrorText = "Ingen registrert badetemperatur for dette badestedet",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            val timeSince = Duration.between(waterTempTime, Instant.now()).toDays()

            Text(
                text = "${waterTemp}° i vannet",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "Sist målt $timeSince dager siden",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    if ((airTemp == null) || (uvIndex == null) || (weatherIcon == null)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = 12.dp)
        ) {
            LoadingIndicator(
                onErrorText = "Kunne ikke hente værinformasjon for badestedet",
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {

            //Shows the current weather as a drawable
            SmallInfoCard(
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = weatherIcon),
                    contentDescription = weatherIcon.toString(),
                    modifier = Modifier.weight(0.5f, fill = true)
                )
                Text(
                    text = "Værforhold",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(0.15f, fill = true)
                )
            }

            //Shows current air temperature
            SmallInfoCard(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "${airTemp.roundToInt()}°",
                    style = MaterialTheme.typography.displayMedium,
                )
                Text(
                    text = "Lufttemperatur",
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            //Shows UV-index and a color based on said value
            SmallInfoCard(
                verticalArrangement = Arrangement.SpaceEvenly,
                clickable = true,
                onClick = { showDialogStateUV.value = true },
            ) {
                Text(
                    text = "${uvIndex?.roundToInt()}",
                    style = MaterialTheme.typography.displayMedium,
                )
                Text(
                    text = "${uvToNorwegian(uvValue)} UV-nivå",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
    Divider(
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
    )
    if (showDialogStateUV.value) {
        Dialog(
            onDismissRequest = { showDialogStateUV.value = false },
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Surface(
//                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "UV-Indeks:", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Lav: 1-2", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Moderat: 3-5", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Sterk: 6-7", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Svært sterk: 8-10", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Ekstrem: 11+", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun SmallInfoCard(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical,
    clickable: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {

    Card(
        modifier = modifier
            .sizeIn(maxHeight = 110.dp, maxWidth = 110.dp)
            .clickable(enabled = clickable, onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = verticalArrangement,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                content()
            }
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