package no.uio.ifi.in2000.team22.badeapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController

private data class NavItem(
    val label: String,
    val description: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val navigationTarget: String,
)

@Composable
fun BadeAppBottomAppBar(navcontroller: NavController, currentNavTarget: String) {

    val items = listOf(
        NavItem(
            label = "Kart",
            description = "Gå til eller åpne kartet",
            iconFilled = Icons.Default.LocationOn,
            iconOutlined = Icons.Outlined.LocationOn,
            navigationTarget = "home"
        ),
        NavItem(
            label = "Søk",
            description = "Gå til eller åpne søk",
            iconFilled = Icons.Default.Search,
            iconOutlined = Icons.Filled.Search,
            navigationTarget = "search"
        ),
        NavItem(
            label = "Favoritter",
            description = "Gå til eller åpne favoritter",
            iconFilled = Icons.Default.Favorite,
            iconOutlined = Icons.Default.FavoriteBorder,
            navigationTarget = "favorites"
        )
    )

    var selectedItem by remember {
        mutableIntStateOf(items.map { it.navigationTarget }.indexOf(currentNavTarget))
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    if (selectedItem == index) {
                        Icon(item.iconFilled, contentDescription = item.description)
                    } else {
                        Icon(item.iconOutlined, contentDescription = item.description)
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navcontroller.navigate(item.navigationTarget) {
                        popUpTo(item.navigationTarget)
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }

//    BottomAppBar(
//        actions = {
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Rounded.LocationOn,
//                    contentDescription = "Åpne eller gå til kartet"
//                )
//            }
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Rounded.Star,
//                    contentDescription = "Åpne eller gå til favoritter"
//                )
//            }
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Rounded.Search,
//                    contentDescription = "Åpne eller gå til søk"
//                )
//            }
//        }
//    )

//    BottomAppBar {
//        for (i in 1..4) {
//            IconButton(
//                onClick = {
//                    if (i == 2) navcontroller.navigate("search")
//                    if (i == 3) navcontroller.navigate("home") {
//                        popUpTo("home")
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                    if (i == 4) navcontroller.navigate("favorites")
//                },
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(100.dp)
//            ) {
//                when (i) {
//                    1 -> Icon(Icons.Filled.Settings, contentDescription = "Instillinger")
//                    2 -> Icon(Icons.Filled.Search, contentDescription = "Søk")
//                    3 -> Icon(Icons.Filled.LocationOn, contentDescription = "Kart")
//                    4 -> Icon(Icons.Filled.Star, contentDescription = "Favoritter")
//                }
//            }
//        }
//    }
}