package no.uio.ifi.in2000.team22.badeapp.ui.components.favorites

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite

@Composable
fun FavoriteButton(
    swimspot: Swimspot,
    favoritesList: List<Swimspot?>,
    modifier: Modifier = Modifier,
    toggleFavorite: () -> Unit,
){
    if (favoritesList.isEmpty()){
        FavoriteButtonOutlined(onClick = toggleFavorite, modifier = modifier)
    } else if (favoritesList.contains(swimspot)){
        FavoriteButtonFilled(onClick = toggleFavorite, modifier = modifier)
    } else {
        FavoriteButtonOutlined(onClick = toggleFavorite, modifier = modifier)
    }
}


@Composable
fun FavoriteButtonOutlined(onClick : () -> Unit, modifier: Modifier = Modifier) {
    val heartColor = Color(red = 255, green = 102, blue = 99)
    OutlinedIconButton (
        modifier = modifier
            .size(60.dp),
        border = BorderStroke(3.dp, heartColor),
        onClick = {
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
fun FavoriteButtonFilled(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val heartColor = Color(red = 255, green = 102, blue = 99)
    val cherryBlossom = Color(red = 255, green = 187, blue = 198, alpha = 255)
    FilledIconButton (
        modifier = modifier
            .size(60.dp),
        colors = IconButtonDefaults.filledIconButtonColors(heartColor),
        onClick = {
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
    FavoriteButtonFilled({})
}

@Composable
@Preview
private fun FavOutlined(){
    FavoriteButtonOutlined({})
}

/*
@Composable
@Preview
private fun FavButtonPreview(){
    val favViewModel: FavoritesScreenViewModel = viewModel()
    val state : FavUiState = favViewModel.favUiState.collectAsState()

    FavoriteButton(
        swimspot = Swimspot(1, "hei", 1.1, 1.1),
        state = state,
        modifier = Modifier
        ) {}
}
*/