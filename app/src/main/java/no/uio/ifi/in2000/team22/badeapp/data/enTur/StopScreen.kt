package no.uio.ifi.in2000.team22.badeapp.data.enTur

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun StoppScreen () {
    val view : EnTurViewModel = viewModel()
    val info by view.stops.collectAsState()
    val stop : StopPlace? = info.stoppList.firstOrNull()

    LaunchedEffect(Unit) {
        if (stop != null) {
            view.getStops(stop.lat, stop.lon)
        }
        Log.d("Screen ", "alt riktig ")

    }
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        item {
            if (stop != null) {
                Text(
                    text = "Stop places at ${stop.label}",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    )
            }
        }
        items(info.stoppList) {
            StopCard(it)
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun StopCard(stop : StopPlace) {
    val colorString = Color(android.graphics.Color.parseColor("#C6E2E9"))

            Card(
                modifier = Modifier
                    .fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = colorString
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Text(
                        text = "Name of stop: ${stop.name}",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        "Category: ${stop.category}",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                }
            }
}





