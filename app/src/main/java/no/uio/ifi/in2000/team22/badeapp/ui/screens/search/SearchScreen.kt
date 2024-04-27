package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var input by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    var seeAllButton by remember { mutableStateOf(true) }
    val (showSuggestions, settSuggestions) = remember { mutableStateOf(true) }
    val scrollState: LazyListState = rememberLazyListState()

    val searchUiState by searchScreenViewModel.searchUiState.collectAsState()
    val locationUiState = searchScreenViewModel.locationUiState.collectAsState()

    val favorites = searchUiState.favorites
    val swimspots = searchUiState.swimspots

    keyboard?.show()

    /* Main search page */
    Scaffold(
        topBar = { Text(text = "") },
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
                )

                { Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Bla helt opp") }
            }
        }

    )
    {
        /* Search functionality */
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
                .size(55.dp),
            state = scrollState
        )
        {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 13.dp, end = 13.dp),
                    value = input,
                    shape = CircleShape,
                    onValueChange = {
                        input = it
                        settSuggestions(input.isEmpty())
                    },

                    label = { Text("Søk her") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Søk") },
                    trailingIcon = {
                        if (input != "")
                            IconButton(onClick = {
                                input = ""
                                settSuggestions(true)
                            }
                            ) {
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
                Spacer(modifier = Modifier.height(10.dp))
            }


            /* HER ER SØK + HJERTE*/
            item {
                val (knapp, knapp2) = remember { mutableStateOf("Se alle") }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left
                ) {

                    if (input == "") {
                        Text(
                            text = "Forslag",
                            modifier = Modifier.padding(start = 5.dp, end = 200.dp),
                            fontSize = 23.sp
                        )
                    } else {
                        Text(text = "", modifier = Modifier.padding(start = 5.dp, end = 280.dp))

                    }

                    TextButton(onClick = {
                        if (knapp == "Se alle") { // når du går fra forslag side til se alle side
                            knapp2("Tilbake")
                            seeAllButton = false
                            settSuggestions(false)
                            input = ""
                            keyboard?.hide()
                        } else { //Nå du går fra Se alle til forslag siden
                            knapp2("Se alle")
                            seeAllButton = true
                            settSuggestions(true)
                            input = ""
                        }
                    })
                    {
                        Text(
                            text = knapp,
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
            val location = locationUiState.value.lastKnownLocation

            if (location != null) {
                searchScreenViewModel.updateSuggestions(
                    location.latitude,
                    location.longitude
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                if (input == "") {
                    if (showSuggestions) {
                        val spots = if (searchUiState.nearestSwimspots.size > 5) {
                            searchUiState.nearestSwimspots.subList(0, 4)
                        } else {
                            searchUiState.nearestSwimspots
                        }
                        ShowFiveSuggestions(
                            navcontroller,
                            spots
                        )
                    }
                } else {
                    swimspots.forEach {
                        var isFavorite = favorites.contains(Favorite(it.id))
                        val toggleFavorite =
                            if (favorites.isEmpty() || !isFavorite) {
                                {
                                    searchScreenViewModel.addFavorite(Favorite(it.id))
                                    isFavorite = !isFavorite
                                }
                            } else {
                                {
                                    searchScreenViewModel.removeFavorite(Favorite(it.id))
                                    isFavorite = !isFavorite
                                }
                            }
                        val onFavoriteClick: () -> Unit = { toggleFavorite() }

                        if (it.name.startsWith(input, ignoreCase = true) && input != "") {
                            ResultCard(
                                navcontroller = navcontroller,
                                swimspot = it,
                                isFavorite = isFavorite,
                                onFavoriteClick = onFavoriteClick
                            )
                        } else if (!seeAllButton && input == "") {
                            ResultCard(
                                navcontroller = navcontroller,
                                swimspot = it,
                                isFavorite = isFavorite,
                                onFavoriteClick = onFavoriteClick
                            )
                        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultCard(
    navcontroller: NavController,
    swimspot: Swimspot,
    distance: Float = 0f,
    isFavorite: Boolean? = null,
    onFavoriteClick: (() -> Unit)? = null
){
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
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = swimspot.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                )
                if (distance.toInt() != 0) {
                    Text(
                        text = "Avstand: ${(distance / 1000).roundToInt()}km",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                if (isFavorite != null && onFavoriteClick != null) {
                    FavoriteButton(Color.Red, isFavorite, onFavoriteClick)
                }
                else {
                    Icon(
                        Icons.Filled.Place,
                        contentDescription = "Pil",
                        modifier = Modifier.align(
                            Alignment.TopEnd
                        )
                    )
                }
            }
        }
    }
}


@Composable
fun hentFemForslag(swimspots: List<Swimspot>): MutableList<Swimspot> {
    val newList = mutableListOf<Swimspot>()

    for (i in 1..5) {
        newList.add(swimspots.random())
    }
    return newList
}

@Composable
fun ShowFiveSuggestions(navcontroller: NavController, swimspots: List<Pair<Swimspot, Float>>) {
    swimspots.forEach {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                ResultCard(navcontroller = navcontroller, swimspot = it.first, distance = it.second)
            }
        }
    }
}




