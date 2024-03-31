package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navcontroller : NavController, swimSpots : List<SwimSpot>){
    //val list = swimSpots
    val list : List<SwimSpot> = listOf(SwimSpot(0, "Åkrasand", 59.250681, 5.194152), SwimSpot(1, "Stavasand", 59.232681, 5.184657), SwimSpot(2, "Fotvatnet", 59.298138, 5.286767), SwimSpot(3, "Sandvesand",59.171176, 5.195650 ))

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
                // FavCard(it, innerPadding)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavCard(swimSpot: SwimSpot, padding : PaddingValues){

    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 100.dp)
            .padding(padding)
            .padding(start = 15.dp, end = 15.dp),
    ) {
        Text(
            text = "Elevated, hallo",
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )

        Text(text = "hei")
    }

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
            .padding(start = 15.dp, end = 15.dp),
        content = {
            Row (
                horizontalArrangement = Arrangement.Center,

                ){
                Card (

                ){
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "",
                        modifier = Modifier
                            //.width(100.dp)
                            //.height(100.dp)
                            .padding(padding)
                    )
                }
                Text(text = swimSpot.name)
            }
        }
    )
    
}




    
    


/*

@Preview
@Composable
fun FavoriteScreenPreview(){
    val navController : NavController = rememberNavController()
    val list : List<SwimSpot> = listOf(SwimSpot(0, "Åkrasand", 59.250681, 5.194152), SwimSpot(1, "Stavasand", 59.232681, 5.184657), SwimSpot(2, "Fotvatnet", 59.298138, 5.286767), SwimSpot(3, "Sandvesand",59.171176, 5.195650 ))
    FavoritesScreen(navController, list)
}

 */
