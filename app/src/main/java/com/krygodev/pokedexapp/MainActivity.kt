package com.krygodev.pokedexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

                    }
                    composable("$POKEMON_DETAILS_SCREEN/{$POKEMON_COLOR}/{$POKEMON_NAME}") {

                    }
                }
            }
        }
    }
}