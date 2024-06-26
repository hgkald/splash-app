package no.uio.ifi.in2000.team22.badeapp.ui.components.mapElements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather
import no.uio.ifi.in2000.team22.badeapp.ui.components.loading.LoadingIndicator
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.AlertIcon
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.WeatherIcon
import kotlin.math.roundToInt

@Composable
fun WeatherInfoButton(
    weather: Weather,
    alerts: List<Alert>,
    onClick: () -> Unit,
    modifier: Modifier
) {
    ElevatedButton(
        contentPadding = PaddingValues(0.dp),
        onClick = { onClick() },
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .width(intrinsicSize = IntrinsicSize.Min)
    )
    {
        if (weather.airTemperature != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxSize()
            ) {
                WeatherIcon(
                    weather = weather,
                    modifier = Modifier
                        .padding(5.dp)
                )
                if (alerts.isNotEmpty()) {
                    AlertIcon(
                        alert = alerts.sortedBy { it.riskMatrixColor }[alerts.lastIndex],
                        modifier = Modifier
                            .padding(0.dp)
                            .size(26.dp)
                            .offset(x = (-13).dp, y = (-9).dp)
                    )
                }

                Text(
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    text = weather.airTemperature.roundToInt().toString()
                        .replace(".", ",") + "\u00b0C",
                )
            }
        } else {
            LoadingIndicator(
                onErrorText = "",
                modifier = Modifier.size(25.dp)
            )
        }
    }
}