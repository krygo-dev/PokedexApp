package com.krygodev.pokedexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.krygodev.pokedexapp.presentation.pokemon_details.PokemonDetailsScreen
import com.krygodev.pokedexapp.presentation.pokemon_details.PokemonDetailsViewModel
import com.krygodev.pokedexapp.presentation.pokemon_list.PokemonListScreen
import com.krygodev.pokedexapp.presentation.pokemon_list.PokemonListViewModel
import com.krygodev.pokedexapp.ui.theme.PokedexAppTheme
import com.krygodev.pokedexapp.util.Constants.POKEMON_COLOR
import com.krygodev.pokedexapp.util.Constants.POKEMON_DETAILS_SCREEN
import com.krygodev.pokedexapp.util.Constants.POKEMON_LIST_SCREEN
import com.krygodev.pokedexapp.util.Constants.POKEMON_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = POKEMON_LIST_SCREEN
                ) {
                    composable(POKEMON_LIST_SCREEN) {
                        val viewModel = hiltViewModel<PokemonListViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        PokemonListScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            navController = navController
                        )
                    }
                    composable(
                        route = "$POKEMON_DETAILS_SCREEN/{$POKEMON_COLOR}/{$POKEMON_NAME}",
                        arguments = listOf(navArgument(POKEMON_COLOR) { type = NavType.IntType })
                    ) {
                        val viewModel = hiltViewModel<PokemonDetailsViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        PokemonDetailsScreen(state = state)
                    }
                }
            }
        }
    }
}