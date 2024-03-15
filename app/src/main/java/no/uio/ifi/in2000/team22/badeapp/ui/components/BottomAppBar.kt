package no.uio.ifi.in2000.team22.badeapp.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BadeAppBottomAppBar(navcontroller : NavController) {

    BottomAppBar {
        for (i in 1..4) {
            IconButton(
                onClick = {
                    if(i == 2)navcontroller.navigate("search")
                    if(i == 3)navcontroller.navigate("home")
                    if(i == 4)navcontroller.navigate("favorites")
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
            ) {
                when (i) {
                    1 -> Icon(Icons.Filled.Settings, contentDescription = "Instillinger")
                    2 -> Icon(Icons.Filled.Search, contentDescription = "Søk")
                    3 -> Icon(Icons.Filled.LocationOn, contentDescription = "Kart")
                    4 -> Icon(Icons.Filled.Star, contentDescription = "Favoritter")
                }
            }
        }
    }
}