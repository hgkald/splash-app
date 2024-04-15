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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@Composable
fun FavoritesScreen(
    navController: NavController,
    //favoriteList: MutableList<Swimspot>,
    favViewModel : FavoritesScreenViewModel = viewModel()
){


    val state by favViewModel.favUiState.collectAsState()

    /*
    Button(onClick = {
        val list = mutableListOf(
            Swimspot(0, "Åkrasand", 59.250681, 5.194152),
            Swimspot(1, "Stavasand", 59.232681, 5.184657),
            Swimspot(2, "Fotvatnet", 59.298138, 5.286767),
            Swimspot(3, "Sandvesand",59.171176, 5.195650)
        )
        list.forEach{
            favViewModel.favorite(it)
        }
    }){
        Text(text = "Legg til favoritter")
    }

     */

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
                    state = state,
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
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavCard(
    swimspot: Swimspot,
    state: FavUiState,
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
                    state = state,
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






















