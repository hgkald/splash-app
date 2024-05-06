package no.uio.ifi.in2000.team22.badeapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController

sealed class Screen(
    val navigationTarget: String,
    val label: String,
    val description: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector
) {
    object Home : Screen(
        navigationTarget = "home",
        label = "Kart",
        description = "Gå til eller åpne kartet",
        iconFilled = Icons.Default.LocationOn,
        iconOutlined = Icons.Outlined.LocationOn
    )

    object Search : Screen(
        navigationTarget = "search",
        label = "Søk",
        description = "Gå til eller åpne søk",
        iconFilled = Icons.Default.Search,
        iconOutlined = Icons.Outlined.Search,
    )

    object Favorites : Screen(
        navigationTarget = "favorites",
        label = "Favoritter",
        description = "Gå til eller åpne favoritter",
        iconFilled = Icons.Default.Favorite,
        iconOutlined = Icons.Outlined.FavoriteBorder,
    )
}


@Composable
fun BadeAppBottomAppBar(navcontroller: NavController, screen: Screen?) {
    val items = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Favorites,
    )

    var selectedItem by remember { mutableIntStateOf(items.indexOf(screen)) }

    BottomAppBar {
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
                    alwaysShowLabel = false,
                    onClick = {
                        navcontroller.navigate(item.navigationTarget) {
                            if (item.navigationTarget == "home") {
                                popUpTo(item.navigationTarget)
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        selectedItem = index
                    }
                )
            }
        }
    }
}