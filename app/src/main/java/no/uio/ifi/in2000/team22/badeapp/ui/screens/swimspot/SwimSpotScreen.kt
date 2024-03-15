package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar

@Composable
@Preview(showSystemUi = true)
fun SwimSpotPreview() {
    SwimSpotScreen()
}

@Composable
fun SwimSpotScreen(swimSpotViewModel: SwimSpotViewModel = viewModel()) {
    val spot: SwimSpot = SwimSpot(name = "Sørenga", lat = 10.0, lon = 20.1)

    Scaffold(
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar() }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(start = 15.dp, end = 15.dp)
        ) {
            Text(
                style = MaterialTheme.typography.displayMedium,
                text = spot.name
            )
        }
    }
}