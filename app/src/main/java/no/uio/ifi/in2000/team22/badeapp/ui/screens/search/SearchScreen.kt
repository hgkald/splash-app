package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.Screen
import no.uio.ifi.in2000.team22.badeapp.ui.components.swimspot.SwimspotCard

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    navcontroller: NavController,
    searchScreenViewModel: SearchScreenViewModel
) {
    val focusManager = LocalFocusManager.current
    val scrollState: LazyListState = rememberLazyListState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val searchUiState by searchScreenViewModel.searchUiState.collectAsState()

    val visibleSwimspots = searchScreenViewModel.filteredSwimspots.collectAsState()

    val favorites = searchUiState.favorites
    val input = searchUiState.searchInput

    val freshwaterOnly = searchUiState.freshwaterOnly
    val saltwaterOnly = searchUiState.saltwaterOnly

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
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            state = scrollState,
            modifier = Modifier
                .padding(it)
                .padding(12.dp)
        ) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
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
                            .padding(start = 16.dp)
                            .fillMaxWidth(0.5f)
                    )

                    OutlinedButton(
                        onClick = { showBottomSheet = true },
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        )
                        {
                            Text("Filter")
                            Icon(Icons.Default.ArrowDropDown, "Åpne filter")
                        }
                    }

                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
            }

            items(visibleSwimspots.value) { swimspot ->
                var isFavorite = favorites.contains(Favorite(swimspot.id))
                val toggleFavorite =
                    if (favorites.isEmpty() || !isFavorite) {
                        {
                            searchScreenViewModel.addFavorite(swimspot.id)
                            isFavorite = !isFavorite
                        }
                    } else {
                        {
                            searchScreenViewModel.removeFavorite(swimspot.id)
                            isFavorite = !isFavorite
                        }
                    }
                val onFavoriteClick: () -> Unit = { toggleFavorite() }

                SwimspotCard(
                    navcontroller = navcontroller,
                    swimspot = swimspot,
                    isFavorite = isFavorite,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
        ) {
            @Composable
            fun FilterChipText(text: String) {
                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Visible,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            val filterChipModifier = Modifier.padding(horizontal = 2.dp)

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Vanntype",
                    style = MaterialTheme.typography.titleMedium,
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    FilterChip(
                        onClick = { searchScreenViewModel.toggleFreshwaterOnly() },//freshwaterOnly = !freshwaterOnly },
                        label = { FilterChipText(text = "Ferskvann") },
                        selected = freshwaterOnly,
                        modifier = filterChipModifier
                    )
                    FilterChip(
                        onClick = { searchScreenViewModel.toggleSaltwaterOnly()}, // saltwaterOnly = !saltwaterOnly },
                        label = { FilterChipText(text = "Saltvann") },
                        selected = saltwaterOnly,
                        modifier = filterChipModifier
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Fasiliteter",
                    style = MaterialTheme.typography.titleMedium,
                )
                val facilities = listOf(
                    "Toaletter",
                    "Kiosk",
                    "Parkeringsplass",
                )
                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    facilities.forEach {
                        FilterChip(
                            onClick = { },
                            label = { FilterChipText(text = it)},
                            selected = false,
                            modifier = filterChipModifier
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Badeplass",
                    style = MaterialTheme.typography.titleMedium,
                )
                val badeplass = listOf(
                    "Strand",
                    "Svaberg",
                    "Stupetårn",
                    "Brygge"
                )
                FlowRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    badeplass.forEach {
                        FilterChip(
                            onClick = { },
                            label = { FilterChipText(text = it)},
                            selected = false,
                            modifier = filterChipModifier
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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





