package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites


import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team22.badeapp.MainActivity
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.favorites.FavoriteButton

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
import no.uio.ifi.in2000.team22.badeapp.ui.components.Screen
import no.uio.ifi.in2000.team22.badeapp.ui.components.swimspot.SwimspotCard


@Composable
fun FavoritesScreen(
    navController: NavController,

    //favoritesList: List<Swimspot>,
    favViewModel : FavoritesScreenViewModel = viewModel()
){

    val state by favViewModel.favUiState.collectAsState()

    Scaffold (
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar(navcontroller = navController) }
    ){ paddingValues ->

        LazyColumn (
            modifier = Modifier
                .padding(paddingValues)
        ){
            items(state.favList){
                FavCard(
                    swimspot = it,
                    favoritesList = state.favList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            top = 0.dp,
                            end = 10.dp,
                            bottom = 16.dp
                        ),
                    toggleFavorite = {
                        favViewModel.toggleFavorite(it)
                    },
                    goToSwimspotScreen = {} //TODO
                )

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
                                viewModel.delete(swimspot.id)
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
                                            viewModel.insert(swimspot.id)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavCard(
    swimspot: Swimspot,
    favoritesList: List<Swimspot>,
    toggleFavorite : () -> Unit,
    goToSwimspotScreen : () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        onClick = {goToSwimspotScreen()}
    ) {

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ){
            FavCardInfo(swimspot)

            Column (
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 27.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                FavoriteButton(
                    swimspot = swimspot,
                    favoritesList = favoritesList,
                    toggleFavorite = toggleFavorite
                )
            }

        }

    }
}


@Composable
fun FavCardInfo(swimspot: Swimspot){
    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(0.7f)
    ){
        Text(
            text = swimspot.name,
            style = MaterialTheme.typography.titleLarge
        )

        Divider(modifier = Modifier.padding(bottom = 8.dp))

        Text(
            text = "Saltvatn eller ferskvatn", // TODO
            style = MaterialTheme.typography.bodyLarge
            )
        Text(
            text = "[Distanse her]", // TODO
            style = MaterialTheme.typography.bodyLarge
        )
    }
}









@Composable
@Preview
fun FavScreenPreview(){
    val navController : NavController = rememberNavController()
    //FavoritesScreen(navController = navController, this@MainActivity)
}











