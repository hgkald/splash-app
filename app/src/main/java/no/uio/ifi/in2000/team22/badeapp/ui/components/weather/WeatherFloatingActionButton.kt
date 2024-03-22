package no.uio.ifi.in2000.team22.badeapp.ui.components.weather

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team22.badeapp.model.forecast.Weather

@Composable
fun WeatherFloatingActionButton(weather: Weather, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
    ){
        WeatherIcon(
            weather = weather,
            modifier = Modifier
                .height(60.dp)
                .padding(12.dp)
        )
    }
}