package no.uio.ifi.in2000.team22.badeapp.ui.components.weather

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather
import no.uio.ifi.in2000.team22.badeapp.ui.screens.home.WeatherUiState

@Composable
fun WeatherDialog(
    weatherUiState: State<WeatherUiState>,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(),
        ) {
            val weather = weatherUiState.value.weather
            val metAlerts = weatherUiState.value.metAlerts
            Log.i("WeatherDialog: WEATHER", weather.toString())
            Log.i("WeatherDialog: METALERTS", metAlerts.toString())

            Column (
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
            ){
                WeatherDialogTitle()

                TemperatureAndIcon(weather)

                PrecipitationText(weather)

                MetAlertInfo(metAlerts)
            }
        }
    }
}

@Composable
fun WeatherDialogTitle() {
    Text(
        modifier = Modifier.padding(6.dp),
        text = "Vær i området",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight(600)
    )
}

@Composable
fun TemperatureAndIcon(weather: Weather) {
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){

        Text(
            text = weather.airTemperature.toString().replace(".",",") + "\u00b0C",
            modifier = Modifier
                .wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )

        WeatherIcon(
            weather = weather,
            modifier = Modifier
                .height(60.dp)
                .padding(8.dp)
        )
    }
}

@Composable
fun PrecipitationText(weather: Weather) {
    if (weather.precipitationNextHour != null) {
        var precipText = "Ingen nedbør forventet"
        if (weather.precipitationNextHour > 0) {
            precipText = "${weather.precipitationNextHour} mm forventet neste time"
                .replace(".", ",")
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
                .wrapContentSize(Alignment.Center),
            text = precipText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}


@Composable
fun MetAlertInfo(alerts: List<Alert>) {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (alerts.isNotEmpty()) {
                val alert = alerts[alerts.lastIndex]
                AlertIcon(alert = alert, modifier = Modifier.padding(12.dp))
                Column {
                    Text(
                        text = alert.eventAwarenessName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight(600)
                    )
                    Text(
                        text = "${alert.riskMatrixColor.norsk} nivå",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

