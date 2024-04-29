package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.Screen
import no.uio.ifi.in2000.team22.badeapp.ui.components.swimspot.SwimspotCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel : FavoritesViewModel,
    swimspotsRepository: SwimspotsRepository
){

    val state by viewModel.favoritesUiState.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val favorites = state.favorites

    Scaffold (
        topBar = {
            Text(
                text = "Mine favoritter",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(24.dp)
            )
        },
        bottomBar = { BadeAppBottomAppBar(navcontroller = navController, Screen.Favorites) },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ){ paddingValues ->
        LazyColumn (
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ){
            favorites.forEach { swimspot ->
                item {
                    if (swimspot != null) {
                        SwimspotCard(
                            navcontroller = navController,
                            swimspot = swimspot,
                            isFavorite = true,
                            onFavoriteClick = {
                                viewModel.delete(Favorite(swimspot.id))
                                scope.launch {
                                    val result = snackbarHostState
                                        .showSnackbar(
                                            message = "${swimspot.name} har blitt slettet fra dine favoritter.",
                                            actionLabel = "Angre",
                                            duration = SnackbarDuration.Long,
                                            withDismissAction = true
                                        )
                                    when (result) {
                                        SnackbarResult.ActionPerformed -> {
                                            viewModel.insert(Favorite(swimspot.id))
                                        }

                                        SnackbarResult.Dismissed -> {
                                            /* Handle snackbar dismissed */
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
