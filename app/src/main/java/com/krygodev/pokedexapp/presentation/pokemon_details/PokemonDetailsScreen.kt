package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PokemonDetailsScreen(
    state: PokemonDetailsState
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Text(text = "Name: ${state.pokemonName}")
            Text(text = "Color: ${state.pokemonDominantColor}")

            Text(text = "Loaded: ${state.pokemon?.name}")
            Text(text = "Error: ${state.loadError}")
        }
    }
}