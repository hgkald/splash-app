package no.uio.ifi.in2000.team22.badeapp.ui.components.swimspot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimspotType
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwimspotCard(
    navcontroller: NavController,
    swimspot: Swimspot,
    isFavorite: Boolean? = null,
    onFavoriteClick: (() -> Unit)? = null
){
    val distance = swimspot.distance

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        onClick = { navcontroller.navigate("swimspot/${swimspot.id}") },
        colors = CardDefaults.cardColors()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(start = 6.dp)
                    .fillMaxWidth(0.7f)
            ) {
                Text(
                    text = swimspot.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(4.dp))
                if(swimspot.type != SwimspotType.UNKNOWN) {
                    Text(
                        text = swimspot.type.norsk,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (isFavorite != null && onFavoriteClick != null) {
                        FavoriteButton(Color.Red, isFavorite, onFavoriteClick)
                    } else {
                        Icon(
                            Icons.Filled.Place,
                            contentDescription = "Pil",
                            modifier = Modifier.align(
                                Alignment.TopEnd
                            )
                        )
                    }
                }
                if (distance != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                    ) {
                        Text(
                            text = "${(distance / 1000).roundToInt()} km unna",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteButton(color: Color = Color.Red, isFavorite: Boolean, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxSize()) {
        Card(onClick = onClick) {
            Icon(
                tint = color, imageVector = if (isFavorite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = "Legg til i favoritter"
            )
        }
    }
}