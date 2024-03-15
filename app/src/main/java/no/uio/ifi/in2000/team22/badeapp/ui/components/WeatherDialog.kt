package no.uio.ifi.in2000.team22.badeapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import no.uio.ifi.in2000.team22.badeapp.model.forecast.CurrentWeather
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
                .height(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            val weather = weatherUiState.value.currentWeather

            Column (
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Center,
            ){
                WeatherDialogTitle()

                TemperatureAndIcon(weather)

                PrecipitationText(weather)

                MetAlertInfo()
            }
        }
    }
}

@Composable
fun WeatherDialogTitle() {
    Text(
        modifier = Modifier.padding(6.dp),
        text = "Weather in this area",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight(600)
    )
}

@Composable
fun MetAlertInfo() {

}

@Composable
fun TemperatureAndIcon(weather: CurrentWeather) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){

        Text(
            text = weather.airTemperature.toString() + "\u00b0C",
            modifier = Modifier
                .wrapContentSize(Alignment.Center),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )

        WeatherIcon(
            weather = weather,
            modifier = Modifier
                .height(60.dp)
                .padding(12.dp)
        )
    }
}

@Composable
fun PrecipitationText(weather: CurrentWeather) {
    if (weather.precipitationNextHour != null) {
        var precipText = "No precipitation expected"
        if (weather.precipitationNextHour > 0) {
            precipText = "${weather.precipitationNextHour} mm expected in the next hour"
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp, 0.dp)
                .wrapContentSize(Alignment.Center),
            text = precipText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}