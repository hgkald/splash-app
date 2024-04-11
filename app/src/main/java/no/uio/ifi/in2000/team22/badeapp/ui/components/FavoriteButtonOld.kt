package no.uio.ifi.in2000.team22.badeapp.ui.components.Favorites

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites.FavUiState
import no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites.FavoritesViewModel


@Composable
fun FavToggleButtonOld(
    swimspot: Swimspot,
    state: State<FavUiState>,
    onClickAddFavorite: () -> Unit,
    onClickRemoveFavorite: () -> Unit
) {
    if (swimspot in state.value.favList){
        FavoriteButtonFilled(swimspot, onClickRemoveFavorite)
    } else {
        FavoriteButtonOutlined(swimspot, onClickAddFavorite)
    }
}

@Composable
fun FavoriteButtonOutlinedOld(swimspot: Swimspot, onClick: () -> Unit) {
    val heartColor = Color(red = 255, green = 102, blue = 99)
    OutlinedIconButton (
        modifier = Modifier
            .size(60.dp),
        border = BorderStroke(3.dp, heartColor),
        onClick = {
            Log.d("FavoriteButton", "Trying to favorite $swimspot")
            onClick()
        }
    ){

        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            tint = heartColor,
            contentDescription ="Favorite",
            modifier = Modifier
                .size(36.dp)
        )
    }
}

@Composable
fun FavoriteButtonFilledOld(swimspot: Swimspot, onClick: () -> Unit) {
    val heartColor = Color(red = 255, green = 102, blue = 99)
    val cherryBlossom = Color(red = 255, green = 187, blue = 198, alpha = 255)
    FilledIconButton (
        modifier = Modifier
            .size(60.dp),
        colors = IconButtonDefaults.filledIconButtonColors(heartColor),
        onClick = {
            Log.d("FavoriteButton", "Trying to unfavorite $swimspot")
            onClick()
        }
    ){

        Icon(
            imageVector = Icons.Filled.Favorite,
            tint = cherryBlossom,
            contentDescription ="Favorite",
            modifier = Modifier
                .size(36.dp)
        )
    }
}

@Composable
@Preview
private fun FavFilled(){
    FavoriteButtonFilled(Swimspot(0, "", 0.0, 0.0), viewModel())
}

@Composable
@Preview
private fun FavOutlined(){
    FavoriteButtonOutlined(Swimspot(0, "", 0.0, 0.0), viewModel())
}

@Composable
@Preview
private fun FavToggle(){
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val state : State<FavUiState> = favoritesViewModel.favUiState.collectAsState()
    val list : List<Swimspot> = listOf(Swimspot(0, "Åkrasand", 59.250681, 5.194152), Swimspot(1, "Stavasand", 59.232681, 5.184657), Swimspot(2, "Fotvatnet", 59.298138, 5.286767), Swimspot(3, "Sandvesand",59.171176, 5.195650 ))
    FavToggleButton(Swimspot(0, "", 0.0, 0.0), state, {}, {})
}