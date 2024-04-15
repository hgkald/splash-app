package no.uio.ifi.in2000.team22.badeapp

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team22.badeapp.data.SwimspotDataSource
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesListRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot
import no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites.FavoritesScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites.FavoritesScreenViewModel
import no.uio.ifi.in2000.team22.badeapp.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.home.HomeScreenViewModel
import no.uio.ifi.in2000.team22.badeapp.ui.screens.search.SearchScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot.SwimspotScreen
import no.uio.ifi.in2000.team22.badeapp.ui.theme.BadeappTheme

class MainActivity : ComponentActivity() {
    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    private val favoritesScreenViewModel: FavoritesScreenViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            homeScreenViewModel.updatePermissionState(isGranted)
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        setContent {
            BadeappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModelStore = checkNotNull(LocalViewModelStoreOwner.current)
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { entry ->
                            val backStackEntry = remember(entry) { navController.getBackStackEntry("home") }
                            HomeScreen(navController, homeScreenViewModel)
                        }

                        composable("favorites") { FavoritesScreen(navController, favoritesScreenViewModel) }
                        composable("search") { SearchScreen(navController) }
                        composable("swimspot") { SwimspotScreen(navController) }
                        //settings?
                    }
                    val x = SwimspotDataSource(this)
                    x.lesFraFil()
                }
            }
        }
    }
}
