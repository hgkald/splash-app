package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar

@Composable
fun FavoritesScreen(navcontroller: NavController) {
    Scaffold(
        topBar = { Text(text = "Favorites") },
        bottomBar = { BadeAppBottomAppBar(navcontroller, "favorites") }
    ) { innerPadding ->
        innerPadding
    }
}