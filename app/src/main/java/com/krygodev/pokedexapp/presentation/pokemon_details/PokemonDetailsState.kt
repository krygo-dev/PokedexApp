package com.krygodev.pokedexapp.presentation.pokemon_details

import androidx.compose.ui.graphics.Color
import com.krygodev.pokedexapp.domain.model.Pokemon

data class PokemonDetailsState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val loadError: String? = null,
    val pokemonName: String = "",
    val pokemonDominantColor: Color? = null
)