package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.screens.home.SwimSpotUiState
import no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot.SwimSpotViewModel


@Composable
fun FavoritesScreen(
    navcontroller : NavController,
    swimspotViewModel: SwimSpotViewModel = viewModel(),
    // swimSpots : List<SwimSpot>
){
    val state: State<SwimSpotUiState> = swimspotViewModel.swimSpotUiState.collectAsState()
    val list : List<SwimSpot> = listOf(SwimSpot(0, "Åkrasand", 59.250681, 5.194152), SwimSpot(1, "Stavasand", 59.232681, 5.184657), SwimSpot(2, "Fotvatnet", 59.298138, 5.286767), SwimSpot(3, "Sandvesand",59.171176, 5.195650 ))
    val swimspotList = state.value.swimSpotList

    Scaffold (
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar(navcontroller) },

        ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            list.forEach{
                FavCard(it)

            }
            Text(text = "Faktiske badesteder:")
            swimspotList.forEach {
                FavCard(swimSpot = it)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavCard(swimSpot: SwimSpot){

    Spacer(modifier = Modifier.padding(5.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f),
    ) {
        val padding = 16


        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            FavCardInfo(swimSpot = swimSpot, padding = padding)
            FavToggleButton(padding)
        }
    }
    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun FavCardInfo(swimSpot: SwimSpot, padding: Int){
    Column (
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .padding(padding.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        FavTitle(swimSpot = swimSpot, padding = padding)

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ){
            FavTemp(padding)
            FavWeather(padding)

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavToggleButton(padding: Int) {
    val heartColor = Color(red = 255, green = 102, blue = 99)

    Row (
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(150.dp)
            .requiredHeight(150.dp)
            .padding(padding.dp)
    ){
        OutlinedIconButton (
            modifier = Modifier
                .size(60.dp),
            border = BorderStroke(1.7.dp, heartColor),
            onClick = {/* TODO */}
        ){

            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                tint = heartColor,
                contentDescription ="Favorite",
                modifier = Modifier
                    .size(25.dp)
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 0.dp, vertical = 30.dp))

    }
}

@Composable
fun FavTitle(swimSpot : SwimSpot, padding : Int){
    Text(
        text = swimSpot.name,
        modifier = Modifier
            .padding(padding.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.titleLarge
    )
}


@Composable
fun FavTemp(padding: Int){
    Text(
        text = "Temperatur: ",
        textAlign = TextAlign.Start,
    )
}


@Composable
fun FavWeather(padding: Int){
    Text(
        text = "Vær: ",
        textAlign = TextAlign.Start,
    )
}



@Preview
@Composable
fun FavoriteScreenPreview(){
    val navController : NavController = rememberNavController()
    val vm : SwimSpotViewModel = viewModel()
    val list : List<SwimSpot> = listOf(SwimSpot(0, "Åkrasand", 59.250681, 5.194152), SwimSpot(1, "Stavasand", 59.232681, 5.184657), SwimSpot(2, "Fotvatnet", 59.298138, 5.286767), SwimSpot(3, "Sandvesand",59.171176, 5.195650 ))
    FavoritesScreen(navController, vm)
}
