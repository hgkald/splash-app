package no.uio.ifi.in2000.team22.badeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesRepository
import no.uio.ifi.in2000.team22.badeapp.data.swimspots.SwimspotsRepository
import no.uio.ifi.in2000.team22.badeapp.persistence.FavoritesDatabase
import no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites.FavoritesScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites.FavoritesViewModel
import no.uio.ifi.in2000.team22.badeapp.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.home.HomeScreenViewModel
import no.uio.ifi.in2000.team22.badeapp.ui.screens.search.SearchScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.search.SearchScreenViewModel
import no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot.SwimspotScreen
import no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot.SwimspotViewModel
import no.uio.ifi.in2000.team22.badeapp.ui.theme.BadeappTheme

class MainActivity : ComponentActivity() {
    private lateinit var swimspotsRepository: SwimspotsRepository
    private lateinit var favoritesRepository: FavoritesRepository
    private lateinit var favoritesDatabase: FavoritesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        swimspotsRepository = SwimspotsRepository(applicationContext)
        favoritesDatabase = FavoritesDatabase.getDatabase(applicationContext)
        favoritesRepository = FavoritesRepository(favoritesDatabase.favoritesDao())

        setContent {
            BadeappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModelStore = checkNotNull(LocalViewModelStoreOwner.current)
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            val homeViewModel: HomeScreenViewModel = viewModel(
                                factory =
                                HomeScreenViewModel.provideFactory(swimspotsRepository)
                            )
                            HomeScreen(navController, homeViewModel)
                        }
                        composable("favorites") {
                            val favoritesViewModel: FavoritesViewModel = viewModel(
                                factory = FavoritesViewModel.provideFactory(
                                    favoritesRepository,
                                    swimspotsRepository
                                )
                            )
                            FavoritesScreen(navController, favoritesViewModel, swimspotsRepository)
                        }
                        composable("search") {
                            val searchViewModel: SearchScreenViewModel = viewModel(
                                factory = SearchScreenViewModel.provideFactory(swimspotsRepository)
                            )
                            SearchScreen(navController, searchViewModel)
                        }
                        composable("swimspot/{swimspotId}") {
                            val swimspotViewModel: SwimspotViewModel = viewModel(
                                factory = SwimspotViewModel.provideFactory(swimspotsRepository)
                            )
                            SwimspotScreen(navController, swimspotViewModel)
                        }
                        //settings?
                    }
                }
            }
        }
    }
}
