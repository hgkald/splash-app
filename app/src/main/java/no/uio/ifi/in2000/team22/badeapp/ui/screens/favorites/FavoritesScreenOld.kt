package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.Favorites.FavToggleButton
import no.uio.ifi.in2000.team22.badeapp.ui.components.loading.LoadingIndicator


@Composable
fun FavoritesScreenOld(
    navcontroller : NavController,
    favoritesViewModel: FavoritesViewModel = viewModel(),
    // swimSpots : List<Swimspot>
){
    val state: State<FavUiState> = favoritesViewModel.favUiState.collectAsState()
    //val list : List<Swimspot> = listOf(Swimspot(0, "Åkrasand", 59.250681, 5.194152), Swimspot(1, "Stavasand", 59.232681, 5.184657), Swimspot(2, "Fotvatnet", 59.298138, 5.286767), Swimspot(3, "Sandvesand",59.171176, 5.195650 ))
    val favList : List<Swimspot> = state.value.favList

    Scaffold (
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar(navcontroller) },
    ){ innerPadding ->
        LazyColumn (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(favList){
                if (favList.size < 1) {
                    LoadingIndicator("Du har ingen favoritter :( \nTrykk på hjerteknappen for å legge til et badested i favorittlisten")
                } else {
                    FavCard(
                        swimspot = it,
                        state = state,
                        onClickFavButton = {favoritesViewModel.toggleFavoriteButton(it)},
                        onClickNav = {TODO()}
                    )
                }

            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavCardOld(
    swimspot: Swimspot,
    state: State<FavUiState>,
    onClickFavButton : () -> Unit,
    onClickNav : () -> Unit
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 16.dp),
        onClick = {
            onClickNav
        }
    ) {
        val padding = 16

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            FavCardInfoOld(swimSpot = swimspot, padding = padding)

            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(150.dp)
                    .requiredHeight(150.dp)
                    .padding(padding.dp)
            ){
                // TODO
                FavToggleButton(swimspot, state, onClickFavButton, onClickFavButton)

                Spacer(modifier = Modifier.padding(horizontal = 0.dp, vertical = 30.dp))
            }
        }
    }
    //Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun FavCardInfoOld(swimSpot: Swimspot, padding: Int){
    Column (
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(padding.dp),
        horizontalAlignment = Alignment.Start
    ){
        FavTitle(swimSpot = swimSpot)

        Divider(modifier = Modifier.padding(bottom = 8.dp))

        FavSwimspotTypeOld(SwimspotType = "Saltvann")
        FavDistanceOld()

        /*
        Row (
            //modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ){
            // FavTemp()
            // FavWeather()
        }
         */
    }
}

/*
@Composable
fun FavToggleButton(padding: Int, button: @Composable () -> Unit
) {
    val heartColor = Color(red = 255, green = 102, blue = 99)

    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(150.dp)
            .requiredHeight(150.dp)
            .padding(padding.dp)
    ){
        button()
        Spacer(modifier = Modifier.padding(horizontal = 0.dp, vertical = 30.dp))

    }
}

 */

@Composable
fun FavTitleOld(swimSpot : Swimspot){
    Text(
        text = swimSpot.name,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.titleLarge
    )
}


@Composable
fun FavTempOld(){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Temperatur: ",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun FavWeatherOld(){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Vær: ",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun FavDistanceOld(){
    Text(
        text = "Badestedet er XX km unna",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun FavSwimspotTypeOld(SwimspotType : String){
    Text(
        text = SwimspotType,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge
    )
}



@Preview
@Composable
fun FavoriteScreenPreviewOld(){
    val navController : NavController = rememberNavController()
    val vm : FavoritesViewModel = viewModel()
    val list : List<Swimspot> = listOf(Swimspot(0, "Åkrasand", 59.250681, 5.194152), Swimspot(1, "Stavasand", 59.232681, 5.184657), Swimspot(2, "Fotvatnet", 59.298138, 5.286767), Swimspot(3, "Sandvesand",59.171176, 5.195650 ))
    FavoritesScreen(navController, vm)
}
