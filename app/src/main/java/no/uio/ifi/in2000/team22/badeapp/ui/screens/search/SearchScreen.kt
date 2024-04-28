package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.filled.Place
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navcontroller: NavController,
    searchScreenViewModel: SearchScreenViewModel
) {
    val keyboard = LocalSoftwareKeyboardController.current
    val scrollState: LazyListState = rememberLazyListState()

    val searchUiState by searchScreenViewModel.searchUiState.collectAsState()
    val locationUiState = searchScreenViewModel.locationUiState.collectAsState()

    val swimspots = searchUiState.swimspots
    val nearestSwimspots = searchUiState.nearestSwimspots

    val favorites = searchUiState.favorites
    val input = searchUiState.searchInput

    keyboard?.show()

    val visibleSwimspots =
        if (input == "" && nearestSwimspots.isNotEmpty()) {
            nearestSwimspots
        }
        else {
            swimspots
        }

    val location = locationUiState.value.lastKnownLocation
    LaunchedEffect(location) {
        if (location != null) {
            searchScreenViewModel.updateNearestSwimspots(location.latitude, location.longitude)
        }
    }

    Scaffold(
        topBar = {
            //Search bar
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                value = input,
                shape = CircleShape,
                onValueChange = {
                    searchScreenViewModel.setInput(it)
                },
                label = { Text("Søk alle badeplasser") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Søk") },
                trailingIcon = {
                    if (input != "")
                        IconButton(onClick = {
                            searchScreenViewModel.setInput("")
                        }) {
                            Icon(Icons.Filled.Close, contentDescription = "Ta bort søk")
                        }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                    }
                )
            )
        },
        bottomBar = { BadeAppBottomAppBar(navcontroller) },
        floatingActionButton = {
            val showButton by remember {
                derivedStateOf { scrollState.firstVisibleItemIndex > 0 }
            }
            AnimatedVisibility(visible = showButton, enter = fadeIn(), exit = fadeOut()) {
                val c = rememberCoroutineScope()
                FloatingActionButton(
                    onClick = {
                        c.launch {
                            scrollState.animateScrollToItem(index = 0)
                        }
                    },
                ) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Bla helt opp")
                }
            }
        }

    )
    {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    val header = if (input == "") {
                        "Alle badeplasser"
                    } else {
                        "Søkeresultater"
                    }
                    Text(
                        text = header,
                        style = MaterialTheme.typography.titleLarge,
                    )

                    /*
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text (
                            style = MaterialTheme.typography.bodySmall,
                            text = "Sortering"
                        )
                        var expanded by remember { mutableStateOf(false) }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Avstand") },
                                onClick = { /* */ },
                                )
                            DropdownMenuItem(
                                text = { Text("Navn") },
                                onClick = { /* Handle settings! */ },
                                )
                        }
                    }*/
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(visibleSwimspots) { spot ->
                var isFavorite = favorites.contains(Favorite(spot.id))
                val toggleFavorite =
                    if (favorites.isEmpty() || !isFavorite) {
                        {
                            searchScreenViewModel.addFavorite(Favorite(spot.id))
                            isFavorite = !isFavorite
                        }
                    } else {
                        {
                            searchScreenViewModel.removeFavorite(Favorite(spot.id))
                            isFavorite = !isFavorite
                        }
                    }
                val onFavoriteClick: () -> Unit = { toggleFavorite() }

                Log.d("SearchScreen", "resultscard")

                if (spot.name.startsWith(input, ignoreCase = true)) {
                    SwimspotCard(
                        navcontroller = navcontroller,
                        swimspot = spot,
                        distance = spot.distance,
                        isFavorite = isFavorite,
                        onFavoriteClick = onFavoriteClick
                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwimspotCard(
    navcontroller: NavController,
    swimspot: Swimspot,
    distance: Float?,
    isFavorite: Boolean? = null,
    onFavoriteClick: (() -> Unit)? = null
){
    val waterTypes = mapOf(
        "FRESH" to "Ferskvann",
        "SALT" to "Saltvann",
        "UNKNOWN" to ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        onClick = { navcontroller.navigate("swimspot/${swimspot.id}") },
        border = BorderStroke(2.dp, Color.LightGray),
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
                    .fillMaxWidth(0.6f)
            ) {
                Text(
                    text = swimspot.name,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(modifier = Modifier.height(4.dp))
                waterTypes[swimspot.type.toString()]?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceAround,
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
                    )}
                }
            }
        }
    }
}

@Composable
fun ShowFiveSuggestions(navcontroller: NavController, swimspots: List<Pair<Swimspot, Float>>) {
    swimspots.forEach {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                SwimspotCard(navcontroller = navcontroller, swimspot = it.first, distance = it.second)
            }
        }
    }
}




