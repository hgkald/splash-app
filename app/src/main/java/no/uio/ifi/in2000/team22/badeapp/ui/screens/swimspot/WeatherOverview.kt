package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.model.uv.UV
import no.uio.ifi.in2000.team22.badeapp.model.uv.doubleToUV
import no.uio.ifi.in2000.team22.badeapp.model.uv.uvToNorwegian
import no.uio.ifi.in2000.team22.badeapp.ui.components.loading.LoadingIndicator
import kotlin.math.roundToInt

@Preview(showSystemUi = true)
@Composable
fun WeatherOverviewPreview() {
    WeatherOverview(
        waterTemp = 20.0,
        airTemp = 21.0,
        uvIndex = 2.0,
        weatherIcon = R.drawable.partlycloudy_day
    )
}

@Composable
fun WeatherOverview(
    waterTemp: Double?,
    airTemp: Double?,
    uvIndex: Double?,
    @DrawableRes weatherIcon: Int?,
) {
    val ready =
        (waterTemp != null) && (airTemp != null) && (uvIndex != null) && (weatherIcon != null)

    Column(
        modifier = Modifier
            .width(400.dp)
    ) {
        if (ready) {
            Content(
                waterTemp, airTemp, uvIndex, weatherIcon
            )
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                LoadingIndicator(
                    onErrorText = "Kunne ikke hente værinformasjon for badestedet"
                )
            }
        }
    }
}


/**
 * Main content for [WeatherOverview]. Containts the main weather info.
 */
@Composable
private fun Content(
    waterTemp: Double?,
    airTemp: Double?,
    uvIndex: Double?,
    @DrawableRes weatherIcon: Int?,
) {
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
        Text(
            text = "${waterTemp?.roundToInt()}° i vannet",
            style = MaterialTheme.typography.displayMedium
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {

        //Shows the current weather as a drawable
        if (weatherIcon != null) {
            SmallInfoCard(
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = weatherIcon),
                    contentDescription = weatherIcon.toString(),
                )
                Text(
                    text = "Værforhold",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }

        //Shows current air temperature
        SmallInfoCard(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "${airTemp?.roundToInt()}°",
                style = MaterialTheme.typography.displayMedium,
            )
            Text(
                text = "Lufttemperatur",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
            )
        }

        //Shows UV-index and a color based on said value
        SmallInfoCard(
//            modifier = Modifier.border(
//                width = 3.dp,
//                color = uvToColor(uvValue),
//                shape = RoundedCornerShape(10)
//            ),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "${uvIndex?.roundToInt()}",
                style = MaterialTheme.typography.displayMedium,
            )
            Text(
                text = "${uvToNorwegian(uvValue)} UV-nivå",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }

    }
    Divider(
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
    )
}

@Composable
fun SmallInfoCard(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .size(110.dp)
    ) {
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            content()
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