package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimspotType
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.Screen
import no.uio.ifi.in2000.team22.badeapp.ui.components.swimspot.SwimspotCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navcontroller: NavController,
    searchScreenViewModel: SearchScreenViewModel
) {
    val focusManager = LocalFocusManager.current
    val scrollState: LazyListState = rememberLazyListState()

    val searchUiState by searchScreenViewModel.searchUiState.collectAsState()
    val locationUiState = searchScreenViewModel.locationUiState.collectAsState()

    val swimspots = searchUiState.swimspots
    val nearestSwimspots = searchUiState.nearestSwimspots

    val favorites = searchUiState.favorites
    val input = searchUiState.searchInput

    var freshwaterOnly by remember { mutableStateOf(false) }
    var saltwaterOnly by remember { mutableStateOf(false) }

    var visibleSwimspots by remember { mutableStateOf(swimspots) }
    visibleSwimspots =
        if (input == "" && nearestSwimspots.isNotEmpty()) {
            nearestSwimspots
        } else {
            swimspots
        }

    LaunchedEffect(freshwaterOnly, saltwaterOnly) {
        visibleSwimspots =
            if (freshwaterOnly && saltwaterOnly) {
                visibleSwimspots
                    .filter { swimspot ->
                        swimspot.type in listOf(
                            SwimspotType.FRESH,
                            SwimspotType.SALT
                        )
                    }
            } else if (!(freshwaterOnly || saltwaterOnly)) {
                visibleSwimspots
            } else {
                visibleSwimspots
                    .filter { swimspot -> if (freshwaterOnly) swimspot.type == SwimspotType.FRESH else true }
                    .filter { swimspot -> if (saltwaterOnly) swimspot.type == SwimspotType.SALT else true }
            }
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
                    .padding(16.dp),
                value = input,
                shape = CircleShape,
                onValueChange = {
                    searchScreenViewModel.setInput(it)
                },
                label = { Text("Søk her") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Søk") },
                trailingIcon = {
                    if (input != "")
                        IconButton(onClick = {
                            searchScreenViewModel.setInput("")
                            focusManager.clearFocus()
                        }) {
                            Icon(Icons.Filled.Close, contentDescription = "Ta bort søk")
                        }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
        },
        bottomBar = { BadeAppBottomAppBar(navcontroller, Screen.Search) },
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
        Column(modifier = Modifier.padding(it)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    //.wrapContentWidth()
            ) {
                val header = if (input == "") {
                    "Finn badeplasser"
                } else {
                    "Søkeresultater"
                }
                        Text(
                            text = header,
                            style = MaterialTheme.typography.titleLarge,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .fillMaxWidth(0.5f)
                                //.weight(1f)
                        )


                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 72.dp),
                    modifier = Modifier.widthIn(min = 72.dp, max = 200.dp)
                ) {
                    //Box(modifier = Modifier
                    //    .fillMaxWidth(0.5f)
                    //) {
                    val filterChipModifier = Modifier.padding(2.dp)

                    //Row (modifier = Modifier.wrapContentSize()){
                    @Composable
                    fun FilterChipText(text: String) {
                        Text(text = text, textAlign = TextAlign.Center, overflow = TextOverflow.Visible, style = MaterialTheme.typography.labelSmall)
                    }
                    item {
                        FilterChip(
                            onClick = { freshwaterOnly = !freshwaterOnly },
                            label = { FilterChipText(text = "Ferskvann") },
                            selected = freshwaterOnly,
                            modifier = filterChipModifier
                        )
                    }
                    item {
                        FilterChip(
                            onClick = { saltwaterOnly = !saltwaterOnly },
                            label = { FilterChipText(text = "Saltvann") },
                            selected = saltwaterOnly,
                            modifier = filterChipModifier
                        )
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(12.dp)
            ) {
                item {

                }


                /*     LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp)
            ) {*/
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
                            isFavorite = isFavorite,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
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
                SwimspotCard(navcontroller = navcontroller, swimspot = it.first)
            }
        }
    }
}





