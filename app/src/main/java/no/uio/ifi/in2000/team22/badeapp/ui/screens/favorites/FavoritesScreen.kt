package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.favorites.FavoriteButton

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel : FavoritesViewModel,
    swimspotsRepository: SwimspotsRepository
){

    val state by viewModel.favoritesUiState.collectAsState()
    val favorites = state.favorites
    val swimspots = state.swimspots

    Scaffold (
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar(navcontroller = navController) }
    ){ paddingValues ->


        LazyColumn (
            modifier = Modifier
                .padding(paddingValues)
        ){
            swimspots.filter { swimspot ->
                favorites.contains(Favorite(swimspot.id))
            }.forEach { swimspot ->
                item {
                    FavCard(
                        swimspot = swimspot,
                        favoritesList = favorites,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 10.dp,
                                top = 0.dp,
                                end = 10.dp,
                                bottom = 16.dp
                            ),
                        onClickFavorite = {
                            viewModel.delete(Favorite(swimspot.id))
                        },
                        onClick = {
                            navController.navigate("swimspot/${swimspot.id}")
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavCard(
    swimspot: Swimspot,
    favoritesList: List<Favorite>,
    onClickFavorite: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        onClick = {onClick()}
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
                    toggleFavorite = onClickFavorite
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

/*
@Composable
@Preview
fun FavScreenPreview(){
    val navController : NavController = rememberNavController()
    //FavoritesScreen(navController = navController, this@MainActivity)
}*/