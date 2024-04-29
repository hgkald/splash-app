package no.uio.ifi.in2000.team22.badeapp.ui.components

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Settings : Screen(
        route = "home",
        label = "Innstillinger",
        icon = Icons.Filled.Settings
    )
    object Search : Screen(
        route = "search",
        label = "Søk",
        icon = Icons.Filled.Search
    )
    object Home : Screen(
        route = "home",
        label = "Kart",
        icon = Icons.Filled.LocationOn
    )
    object Favorites : Screen(
        route = "favorites",
        label = "Favoritter",
        icon = Icons.Filled.Favorite
    )
}


@Composable
fun BadeAppBottomAppBar(navcontroller: NavController, screen: Screen?) {
    val items = listOf(
        Screen.Search,
        Screen.Home,
        Screen.Favorites,
        Screen.Settings
    )

    var selectedItem by remember { mutableIntStateOf(items.indexOf(screen)) }

    BottomAppBar {
        NavigationBar {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = selectedItem == index,
                    alwaysShowLabel = false,
                    onClick = {
                        selectedItem = index
                        navcontroller.navigate(item.route) {
                            if (item == Screen.Home) {
                                popUpTo(item.route)
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
        /*for (i in 1..4) {
            IconButton(
                onClick = {
                    if (i == 2) navcontroller.navigate("search")
                    if (i == 3) navcontroller.navigate("home") {
                        popUpTo("home")
                        launchSingleTop = true
                        restoreState = true
                    }
                    if (i == 4) navcontroller.navigate("favorites")
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
            ) {
                when (i) {
                    1 -> Icon(Icons.Filled.Settings, contentDescription = "Instillinger")
                    2 -> Icon(Icons.Filled.Search, contentDescription = "Søk")
                    3 -> Icon(Icons.Filled.LocationOn, contentDescription = "Kart")
                    4 -> Icon(Icons.Filled.Star, contentDescription = "Favoritter")
                }
            }
        }*/
    }
}