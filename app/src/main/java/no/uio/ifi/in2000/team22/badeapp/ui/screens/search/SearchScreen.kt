package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.persistence.Favorite
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navcontroller: NavController,
    searchScreenViewModel: SearchScreenViewModel
    ) {
    var input by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    var seAlleKnapp by remember { mutableStateOf(true) }
    val (visForslag, settForslag) =  remember { mutableStateOf(true) }
    val scrollState: LazyListState = rememberLazyListState()

    val searchUiState by searchScreenViewModel.searchUiState.collectAsState()
    val favorites = searchUiState.favorites
    val swimspots = searchUiState.swimspots

    keyboard?.show()

    /* Oppsett side */
    Scaffold(
        topBar = { Text(text = "") },
        bottomBar = { BadeAppBottomAppBar(navcontroller) },
        floatingActionButton = {
            val visKnapp by remember {
                derivedStateOf { scrollState.firstVisibleItemIndex > 0  }
            }
            AnimatedVisibility(visible = visKnapp, enter = fadeIn(), exit = fadeOut() ) {
                val c = rememberCoroutineScope()
                FloatingActionButton(onClick = { c.launch {
                    scrollState.animateScrollToItem(index = 0)
                } },)

                { Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Bla helt opp")}
            }
        }

        )
    {
        /* SØKEFUNKSJON */

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
                    onValueChange = { input = it
                        settForslag(input.isEmpty())},

                    label = { Text("Søk her") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Søk") },
                    trailingIcon = {
                        if (input != "")
                            IconButton(onClick = { input = ""
                            settForslag(true)}
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

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.Left) {

                    if(input == "") {
                        Text(text = "Forslag",
                            modifier = Modifier.padding(start = 5.dp, end = 200.dp),
                            fontSize = 23.sp)
                    } else {
                        Text(text = "", modifier = Modifier.padding(start = 5.dp, end = 280.dp))

                    }

                    TextButton(onClick = {
                        if(knapp == "Se alle") { // når du går fra forslag side til se alle side
                            knapp2("Tilbake")
                            seAlleKnapp = false
                            settForslag(false)
                            input = ""
                            keyboard?.hide()
                        }

                        else { //Nå du går fra Se alle til forslag siden
                            knapp2("Se alle")
                            seAlleKnapp = true
                            settForslag(true)
                            input = ""
                        }
                    })
                    { Text(
                        text = knapp,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline
                    )
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
                swimspots.forEach {
                    var isFavorite = favorites.contains(Favorite(it.id))
                    val toggleFavorite =
                        if (favorites.isEmpty() || !isFavorite) {
                            {
                                searchScreenViewModel.addFavorite(Favorite(it.id))
                                isFavorite = !isFavorite
                            }
                        }
                        else {
                            {
                                searchScreenViewModel.removeFavorite(Favorite(it.id))
                                isFavorite = !isFavorite
                            }
                        }
                    val onClick: () -> Unit = { toggleFavorite() }

                    if (it.name.startsWith(input, ignoreCase = true) && input != "") {
                        Kort(navcontroller, it, isFavorite, onClick)
                    } else if (!seAlleKnapp && input == "") {
                        Kort(navcontroller, it, isFavorite, onClick)
                    }
                }

                if (visForslag) {
                    VisFemForslag(navcontroller, searchUiState.swimspots)
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavorittKnapp(color: Color = Color.Red, isFavorite: Boolean, onClick: () -> Unit) {
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
fun Kort(navcontroller: NavController, it: Swimspot, isFavorite: Boolean, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .size(width = 500.dp, height = 100.dp),
        onClick = { navcontroller.navigate("swimspot/${it.id}") },
        border = BorderStroke(2.dp, Color.LightGray)
    )
    {
        Box(contentAlignment = Alignment.TopCenter) {
            Text(
                text = it.name,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
            FavorittKnapp(Color.Red, isFavorite, onClick)
        }
    }
    Spacer(modifier = Modifier.height(13.dp))
}


@Composable
fun hentFemForslag(swimspots: List<Swimspot>): MutableList<Swimspot> {
    val newList = mutableListOf<Swimspot>()

    for (i in 1..5){
        newList.add(swimspots.random())
    }
    return newList
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisFemForslag(navcontroller: NavController, swimspots: List<Swimspot>) {
    val x = hentFemForslag(swimspots)

    x.forEach {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp),
                    onClick = { navcontroller.navigate("swimspot/${it.id}") },
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = it.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                        )
                        Box(modifier = Modifier.fillMaxSize()) {
                            Icon(
                                Icons.Filled.Place,
                                contentDescription = "Pil",
                                modifier = Modifier.align(
                                    Alignment.TopEnd
                                )
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                }

            }
        }
    }
}




