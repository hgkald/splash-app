package no.uio.ifi.in2000.team22.badeapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team22.badeapp.data.SwimspotDataSource
import no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites.FavoritesScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.search.SearchScreen
import no.uio.ifi.in2000.team22.badeapp.ui.theme.BadeappTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        //installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            BadeappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModelStore = checkNotNull(LocalViewModelStoreOwner.current)
                    val navcontroller = rememberNavController()

                    NavHost(navController = navcontroller, startDestination = "home") {
                        composable("home") {
                            HomeScreen(navcontroller)
                        }
                        composable("favorites") { FavoritesScreen(navcontroller) }
                        composable("search") { SearchScreen(navcontroller) }
                        //composable("swimspot") { SwimspotScreen(navcontroller) }
                        //settings?
                    }
                    val x = SwimspotDataSource(this)
                    x.lesFraFil()
                }
            }
        }
    }
}
